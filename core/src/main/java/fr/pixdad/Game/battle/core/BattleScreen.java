package fr.pixdad.Game.battle.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.pixdad.Game.data.DataManager;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.TBSGame;
import fr.pixdad.Game.battle.states.FightingScreenState;
import fr.pixdad.Game.battle.ui.StatsUI;
import fr.pixdad.Game.tiled.maps.TiledScreen;
import fr.pixdad.Game.data.DataManagerOld;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.*;


/**
 * <p>Implementation of the {@link Screen} interface, extending {@link TiledScreen} class.</p>
 * <p>
 * <p>Instantiate a fight between two {@link Player players}, on a board.<br/>
 * Instantiate a state machine which contains the logic of the fight, see {@link FightingScreenState} enum</p>
 * <p>
 * <p>Handles the different entities (board, cursor, {@link Fighter fighters}) and the UI</p>
 */
public class BattleScreen extends TiledScreen implements InputProcessor {

	//region Attributes
	/* =========================================================== */

	public final BattleManager battleManager;
	private final GameLevel level;
	private final BattleState battleState;


	protected HashMap<Fighter, TextureMapObject> fighterMapObjects;

	Stage stageUI;
	StatsUI statsUI;
	Skin skin;
	float UIheight = 200f;


	/* =========================================================== */
	//endregion


	/**
	 * @param game
	 * @param level
	 * @param zoom
	 */
	public BattleScreen(TBSGame game, GameLevel level, float zoom) {
		super(game, level.getMap(), level.getLayers().tilesGround ,zoom);

		this.level = level;
		this.battleState = new BattleState(DataManager.get().getCurrentWorld(), level);
		this.battleManager = new BattleManager(level, battleState, this);

		DataManagerOld.initTextures();
		initMapObjects();
		initStageUI();

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stageUI);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void initMapObjects() {
		//Create & store for convenient use fighter textures
		fighterMapObjects = new HashMap<>();
		for(Fighter fighter : battleState.getAllFighters(true)) {
			TextureMapObject object = new TextureMapObject(fighter.character.getTextureRegion());
			fighterMapObjects.put(fighter, object);
		}

		MapObjects board = battleState.createBoard(level);
		MapObject cursor = battleState.createCursor(cellSize);

		addInLayer(level.getLayers().cells, board);
		addInLayer(level.getLayers().cells, cursor);
	}

	public void initStageUI() {
		this.gutters.BOTTOM = dXwindow2world(UIheight);
		stageUI = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("Skins/default/skin/uiskin.json"));

		statsUI = new StatsUI(stageUI.getWidth(), UIheight, skin);
		stageUI.addActor(statsUI);

	}


	//region Utils Methods
	/* =========================================================== */


	private void emptyLayer(MapLayer layer) {
		Iterator<MapObject> it = layer.getObjects().iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	private void renderFightingUI() {
		emptyLayer(level.getLayers().ui);

		for (Fighter fighter : battleState.getAllFighters(false)) {
			int max = fighter.getCurrentStats().hpMax;
			int hp = fighter.hp;
			float ratio = (float) hp / max;

			TextureMapObject barfg = new TextureMapObject(new TextureRegion(DataManagerOld.getCustomTexture("life bar foreground")));
			TextureMapObject barbg = new TextureMapObject(new TextureRegion(DataManagerOld.getCustomTexture("life bar background")));
			barfg.setX(fighterMapObjects.get(fighter).getX());
			barfg.setY(fighterMapObjects.get(fighter).getY());
			barbg.setX(fighterMapObjects.get(fighter).getX());
			barbg.setY(fighterMapObjects.get(fighter).getY());
			barfg.setScaleX(ratio);
			addInLayer(level.getLayers().ui, barbg);
			addInLayer(level.getLayers().ui, barfg);
		}
	}

	private void renderFighter() {
		emptyLayer(level.getLayers().fighters);
		battleState.getAllFighters(false).forEach(fighter -> {
			TextureMapObject obj = fighterMapObjects.get(fighter);
			obj.setVisible(fighter.isVisible);
			obj.setScaleX(0.9f);
			obj.setScaleY(0.9f);
			obj.setX(fighter.getPosition().world(this).x);
			obj.setY(fighter.getPosition().world(this).y);
			addInLayer(level.getLayers().fighters, obj);
		});
	}

	/* =========================================================== */
	//endregion


	//region Override Methods
	/* =========================================================== */

	@Override
	public void render(float delta) {
		battleManager.update(delta);

		renderFighter();
		renderCamera(delta);
		renderFightingUI();
		renderMap();

		stageUI.act(delta);
		stageUI.draw();
	}


	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.Q) {
			game.closeScreen();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Compute the cell position to send to battleManager
		Rectangle worldRect = new Rectangle(0, 0, worldSizePX.x, worldSizePX.y);
		Rectangle screenRect = new Rectangle(0, 0, screenSizePx.x, screenSizePx.y);
		Vector2 windowPosition = new Vector2(screenX, screenY);

		//Check is there is a valid cell existing at these screen coordinates
		//else, the position isn't updated (last position conserved)
		if (screenRect.contains(windowPosition) && worldRect.contains(window2world(windowPosition))) {
			Vector2 cellPosition = window2cell(windowPosition);
			return battleManager.touchUp((int)cellPosition.x, (int)cellPosition.y, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		//Compute the camera movements (mouse around the edge)
		float dX = dXworld2window(cellSize.x);
		float dY = dYworld2window(cellSize.y);
		moveCameraToLeft = (screenX > -dX && screenX < dX);
		moveCameraToTop = (screenY > -dY && screenY < dY);
		moveCameraToRight = (screenX > screenSizePx.x - dX && screenX < screenSizePx.x + dX);
		moveCameraToBottom = (screenY > screenSizePx.y - dY && screenY < screenSizePx.y + dY);

		//Compute the cell position to send to battleManager
		Rectangle worldRect = new Rectangle(0, 0, worldSizePX.x, worldSizePX.y);
		Rectangle screenRect = new Rectangle(0, 0, screenSizePx.x, screenSizePx.y);
		Vector2 windowPosition = new Vector2(screenX, screenY);

		//Check is there is a valid cell existing at these screen coordinates
		//else, the position isn't updated (last position conserved)
		if (screenRect.contains(windowPosition) && worldRect.contains(window2world(windowPosition))) {
			//battleState.getCursor().setCellPosition(window2cell(position));
			Vector2 cellPosition = window2cell(windowPosition);
			return battleManager.mouseMoved((int)cellPosition.x, (int)cellPosition.y);
		}
		return false;
		//return stageUI.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		camera.translate(amountX, amountY);
		return false;
	}


	/* =========================================================== */
	//endregion

}
