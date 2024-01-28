package fr.pixdad.Game.battle.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.battle.core.BattleState;
import fr.pixdad.Game.battle.core.BoardCellObject;
import fr.pixdad.Game.battle.core.BattleScreen;
import fr.pixdad.Game.battle.core.Player;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class BattlePhase implements State<BattleState>, InputProcessor {

    public static final BattlePhase ASSIGN_POSITIONS_PHASE = new BattlePhase() {

    };
    public static final BattlePhase ACTION_SELECTION_PHASE = new ActionSelectionFState();
    public static final BattlePhase ACTION_COMPUTING_PHASE = new ActionExecutionFState();


    protected StateMachine<BattleState, BattlePhase> stateMachine;
    protected ArrayList<BoardCellObject> modifiedCells;
    protected Player currentPlayer;
    protected BattleState battleState;
    final DefaultGraphPath<BoardCellObject> graphPath = new DefaultGraphPath<>();

    public float delta;


    //region Implement StateMachine
    /* =========================================================== */

    @Override
    public void enter(BattleState battleState) {
        currentPlayer = battleState.currentPlayer();
        modifiedCells = new ArrayList<BoardCellObject>();
    }

    @Override
    public void update(BattleState battleState) {

    }

    /**
     * When any state exited: reset the {@link #modifiedCells} to the BASIC type, & hide the cursor
     */
    @Override
    public void exit(BattleState battleState) {
        for (BoardCellObject cell : modifiedCells) {
            cell.setType(BoardCellObject.CellType.BASIC);
        }
        screen.getCursor().setVisible(false);
    }

    @Override
    public boolean onMessage(BattleState battleState, Telegram telegram) {
        return false;
    }

    /* =========================================================== */
    //endregion


    //region InputProcessor
    /* =========================================================== */

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

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
        battleState.setCursorPosition(screenX, screenY);
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
