package fr.pixdad.Game.fight.states;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.pixdad.Game.data.DataManager;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.fight.Player;
import fr.pixdad.Game.fight.action.Action;
import fr.pixdad.Game.TBSGame;
import fr.pixdad.Game.fight.ui.StatsUI;
import fr.pixdad.Game.tiled.maps.TiledScreen;
import fr.pixdad.Game.tiled.utils.CellMapObject;
import fr.pixdad.Game.tiled.utils.Coordinates;
import fr.pixdad.Game.data.DataManagerOld;
import com.badlogic.gdx.*;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import fr.pixdad.Game.tiled.utils.TiledPathFinder;

import java.util.*;


/**
 * <p>Implementation of the {@link Screen} interface, extending {@link TiledScreen} class.</p>
 * <p>
 * <p>Instantiate a fight between two {@link Player players}, on a board.<br/>
 * Instantiate a state machine which contains the logic of the fight, see {@link FightingScreenState} enum</p>
 * <p>
 * <p>Handles the different entities (board, cursor, {@link Fighter fighters}) and the UI</p>
 */
public class FightingScreen extends TiledScreen implements InputProcessor {

	//region Attributes
	/* =========================================================== */

	public GameLevel level;
	public final TiledPathFinder pathfinder;
	public final DefaultStateMachine<FightingScreen, GlobalFState> stateMachine;
	//Used in stateMachine
	Fighter source;
	CellMapObject target;

	private int _indexCurrentPlayer;
	protected ArrayList<CellMapObject> board;
	protected HashMap<Fighter, TextureMapObject> fighterMapObjects;


	protected CellMapObject cursor;
	int cellCountMargin = 1;

	public ArrayDeque<Action> actions = new ArrayDeque<>();

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
	public FightingScreen(TBSGame game, GameLevel level, float zoom) {
		super(game, level.getMap(), level.getLayers().tilesGround ,zoom);

		this.level = level;

		DataManagerOld.initTextures();
		initMapObjects();
		initStageUI();

		Gdx.input.setInputProcessor(this);
		stateMachine = new DefaultStateMachine<FightingScreen, GlobalFState>(this, FightingScreenState.SELECT_PERSO.get());
		stateMachine.getCurrentState().enter(this);

		this.pathfinder = new TiledPathFinder(this);

		_indexCurrentPlayer = 0;
	}

	public void initMapObjects() {
		cursor = new CellMapObject(0, 0, cellSize);
		cursor.setVisible(true);
		cursor.setName("cursor");
		cursor.setOpacity(0.1f);
		//cursor.setCellPosition(Vector2.Y);

		//Create a board of CellMapObjects to render on available positions
		board = new ArrayList<>();
		for (Vector2 position : getAccessibleCellPositions()) {
			CellMapObject cobject = new CellMapObject((int) position.x, (int) position.y, cellSize);
			cobject.setType(CellMapObject.CellType.BASIC);
			board.add(cobject);
			addInLayer(level.getLayers().cells, cobject);
		}

		addInLayer(level.getLayers().cells, cursor);

		fighterMapObjects = new HashMap<>();
		for(Fighter fighter : level.getAllFighters(true)) {
			TextureMapObject object = new TextureMapObject(fighter.character.getTextureRegion());
			fighterMapObjects.put(fighter, object);
			addInLayer(level.getLayers().fighters, object);
		}
	}

	public void initStageUI() {
		this.gutters.BOTTOM = dXwindow2world(UIheight);
		stageUI = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("Skins/default/skin/uiskin.json"));

		statsUI = new StatsUI(stageUI.getWidth(), UIheight, skin);
		stageUI.addActor(statsUI);

	}

	public Player currentPlayer() {
		if(level.getPlayers().length == 0) return null;
		return level.getPlayers()[_indexCurrentPlayer];
	}
	public Player nextPlayer() {
		return level.getPlayers()[(_indexCurrentPlayer + 1) % level.getPlayers().length];
	}
	public Player moveToNextPlayer() {
		_indexCurrentPlayer = (_indexCurrentPlayer + 1) % level.getPlayers().length;
		currentPlayer().initTurn();
		stateMachine.changeState(FightingScreenState.SELECT_PERSO.get());
		return currentPlayer();
	}

	public void success(Player player) {
		DataManager.get().getCurrentWorld().completeLevel(level);
		game.closeScreen();
	}


	//region Getters & Setters
	/* =========================================================== */

	public GameLevel getLevel() {
		return level;
	}

	public CellMapObject getCursor() {
		return cursor;
	}

	public Coordinates getCursorCoordinates() {
		Coordinates c = new Coordinates(this);
		c.setCellCoordinates(cursor.getCellPosition());
		return c;
	}

	public boolean setCursorPosition(int screenX, int screenY) {
		Vector2 position = new Vector2(screenX, screenY);
		Rectangle worldRect = new Rectangle(0, 0, worldSizePX.x, worldSizePX.y);
		Rectangle screenRect = new Rectangle(0, 0, screenSizePx.x, screenSizePx.y);


		//if the mouse is "in the world"
		//else, the position isn't updated (last position conserved)
		if (screenRect.contains(position) && worldRect.contains(window2world(position))) {
			cursor.setCellPosition(window2cell(position));
			return true;
		}
		return false;
	}

	public ArrayList<CellMapObject> getBoard() {return this.board;}

	public CellMapObject getBoardCell(Vector2 cellposition) {
		for (CellMapObject object : board) {
			if (object.getCellPosition().equals(cellposition)) return object;
		}
		return null;
	}

	public StatsUI getStatsUI() {
		return this.statsUI;
	}

	/* =========================================================== */
	//endregion


	//region Utils Methods
	/* =========================================================== */

	private ArrayList<Vector2> getAccessibleCellPositions() {

		Polyline polyline = ((PolylineMapObject) level.getLayers().cells.getObjects().get("zone_accessible")).getPolyline();
		Polygon polygon = new Polygon(polyline.getVertices());
		polygon.setPosition(polyline.getX(), polyline.getY());

		ArrayList<Vector2> accessibles = new ArrayList<Vector2>();
		for (int i = cellCountMargin; i < level.getSize().x - cellCountMargin; i++) {
			for (int j = cellCountMargin; j < level.getSize().y - cellCountMargin; j++) {

				if (level.isCellPositionAccessible(i, j) && polygon.contains(this.cell2world(new Vector2(i, j))))
					accessibles.add(new Vector2(i, j));
			}
		}
		return accessibles;
	}

	private void emptyLayer(MapLayer layer) {
		Iterator<MapObject> it = layer.getObjects().iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	private void renderFightingUI() {
		emptyLayer(level.getLayers().ui);

		for (Fighter fighter : level.getAllFighters(false)) {
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
		level.getAllFighters(false).forEach(fighter -> {
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

		stateMachine.getCurrentState().delta = delta;
		stateMachine.update();

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
		return stateMachine.getCurrentState().keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (stageUI.touchDown(screenX, screenY, pointer, button)) {
			return true;
		}
		return stateMachine.getCurrentState().touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (stageUI.touchUp(screenX, screenY, pointer, button)) {
			return true;
		}
		return stateMachine.getCurrentState().touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (stageUI.touchDragged(screenX, screenY, pointer)) {
			return true;
		}
		return stateMachine.getCurrentState().touchDragged(screenX, screenY, pointer);
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

		if (stageUI.mouseMoved(screenX, screenY)) {
			return true;
		}
		else {
			setCursorPosition(screenX, screenY);
			return stateMachine.getCurrentState().mouseMoved(screenX, screenY);
		}
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}


	/* =========================================================== */
	//endregion

}


/*
	@Override
	protected void drawRectangleMapObject(RectangleMapObject robject, Batch batch) {
		if (robject instanceof CellMapObject) {
			CellMapObject cobject = (CellMapObject) robject;
			Rectangle rect = cobject.getRectangle();
			Color lineColor = cobject.getLineColor();
			Color fillColor = cobject.getFillColor();

			ShapeRenderer sr = new ShapeRenderer();
			sr.setProjectionMatrix(batch.getProjectionMatrix());

			batch.end();
			if (fillColor != null) {
				fillColor.a *= cobject.getOpacity();
				sr.setColor(fillColor);
				sr.begin(ShapeRenderer.ShapeType.Filled);
				sr.rect(rect.x, rect.y, rect.width, rect.height);
				sr.end();
			}
			if (lineColor == null) {
				lineColor.a *= cobject.getOpacity();
				sr.setColor(lineColor);
				sr.begin(ShapeRenderer.ShapeType.Line);
				sr.rect(rect.x, rect.y, rect.width, rect.height);
				sr.end();
			}

			batch.begin();
		}
		else {
			super.drawRectangleMapObject(robject, batch);
		}

	}
*/