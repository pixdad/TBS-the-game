package fr.pixdad.Game.fight.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.fight.Player;
import fr.pixdad.Game.tiled.utils.CellMapObject;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class GlobalFState implements State<FightingScreen>, InputProcessor {

    protected StateMachine<FightingScreen, GlobalFState> stateMachine;
    protected ArrayList<CellMapObject> modifiedCells;
    protected Player currentPlayer;
    protected FightingScreen screen;
    final DefaultGraphPath<CellMapObject> graphPath = new DefaultGraphPath<>();

    public float delta;

    public boolean searchPath(int range) {
        return searchPath(screen.source.getPosition().cell(), screen.getCursor().getCellPosition(), range, graphPath);
    }
    public boolean searchPath(Vector2 from, Vector2 to, int range, DefaultGraphPath<CellMapObject> graphPath) {
        graphPath.clear();
        screen.pathfinder.searchNodePath(screen.getBoardCell(from), screen.getBoardCell(to), graphPath);
        if (graphPath.getCount() > range || graphPath.getCount() == 0) {
            graphPath.clear();
            return false;
        }
        return true;
    }
    public void showPath() {
        Iterator<CellMapObject> it = modifiedCells.iterator();
        while (it.hasNext()) {
            CellMapObject cell = it.next();
            if (cell.getType() == CellMapObject.CellType.PATH) {
                cell.revertType();
                it.remove();
            }
        }
        for (CellMapObject node : graphPath) {
            modifiedCells.add(node);
            node.setType(CellMapObject.CellType.PATH);
        }
    }


    //region Implement StateMachine
    /* =========================================================== */

    @Override
    public void enter(FightingScreen screen) {
        currentPlayer = screen.currentPlayer();
        stateMachine = screen.stateMachine;
        this.screen = screen;
        modifiedCells = new ArrayList<CellMapObject>();
    }

    @Override
    public void update(FightingScreen screen) {

    }

    /**
     * When any state exited: reset the {@link #modifiedCells} to the BASIC type, & hide the cursor
     */
    @Override
    public void exit(FightingScreen screen) {
        for (CellMapObject cell : modifiedCells) {
            cell.setType(CellMapObject.CellType.BASIC);
        }
        screen.getCursor().setVisible(false);
    }

    @Override
    public boolean onMessage(FightingScreen screen, Telegram telegram) {
        return false;
    }

    /* =========================================================== */
    //endregion


    //region InputProcessor
    /* =========================================================== */

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screen.setCursorPosition(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    /* =========================================================== */
    //endregion



}
