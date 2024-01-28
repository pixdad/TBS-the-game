package fr.pixdad.Game.battle.core;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.battle.utils.CellMapPathFinder;
import fr.pixdad.Game.data.DataManagerOld;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.battle.states.FightingScreenState;
import fr.pixdad.Game.battle.states.BattlePhase;
import fr.pixdad.Game.tiled.utils.Coordinates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BattleManager extends InputAdapter {

    private GameLevel levelData;
    private BattleState battleState;

    private DefaultStateMachine<BattleState, BattlePhase> stateMachine;

    private final CellMapPathFinder pathfinder;

    public BattleManager(GameLevel levelData, BattleState battleState, BattleScreen screen) {
        this.levelData = levelData;
        stateMachine = new DefaultStateMachine<BattleState, BattlePhase>(battleState, FightingScreenState.SELECT_PERSO.get());
        this.pathfinder = new CellMapPathFinder(battleState);

        stateMachine.getCurrentState().enter(battleState);
    }

    public BattlePhase getCurrentState() {
        return stateMachine.getCurrentState();
    }

    public boolean searchPath(int range) {
        return searchPath(screen.source.getPosition().cell(), screen.getCursor().getCellPosition(), range, graphPath);
    }
    public boolean searchPath(Vector2 from, Vector2 to, int range, DefaultGraphPath<BoardCellObject> graphPath) {
        graphPath.clear();
        screen.pathfinder.searchNodePath(screen.getBoardCell(from), screen.getBoardCell(to), graphPath);
        if (graphPath.getCount() > range || graphPath.getCount() == 0) {
            graphPath.clear();
            return false;
        }
        return true;
    }
    public void showPath() {
        Iterator<BoardCellObject> it = modifiedCells.iterator();
        while (it.hasNext()) {
            BoardCellObject cell = it.next();
            if (cell.getType() == BoardCellObject.CellType.PATH) {
                cell.revertType();
                it.remove();
            }
        }
        for (BoardCellObject node : graphPath) {
            modifiedCells.add(node);
            node.setType(BoardCellObject.CellType.PATH);
        }
    }

    public void update(float delta) {


        stateMachine.getCurrentState().delta = delta;
        stateMachine.update();
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchUp(int col, int row, int pointer, int button) {
        battleState.getCursor().setCellPosition(new Vector2(col, row));
        return false;
    }

    @Override
    public boolean mouseMoved(int col, int row) {
        battleState.getCursor().setCellPosition(new Vector2(col, row));
        return false;
    }
}
