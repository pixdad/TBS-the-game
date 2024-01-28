package fr.pixdad.Game.battle.core;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.battle.action.Action;
import fr.pixdad.Game.battle.states.BattlePhase;
import fr.pixdad.Game.data.DataManagerOld;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.data.entities.GameWorld;
import fr.pixdad.Game.tiled.utils.Coordinates;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BattleState {

    public class ActionContext {
        //Context state
        public DefaultGraphPath<BoardCellObject> graphPath;
        public List<BoardCellObject> selected;
        public Deque<Action<Object, Object>> actions;
    }

    private int turnNumber;
    private Player[] players;
    private int _indexCurrentPlayer;
    private List<BoardCellObject> board;

    private ActionContext actionContext;
    private BoardCellObject cursor;


    private BattlePhase currentPhase;

    public BattleState(GameWorld world, GameLevel level) {
        reset(world, level);
    }
    public void reset(GameWorld world, GameLevel level) {
        turnNumber = 0;
        _indexCurrentPlayer = 0;
        players = DataManagerOld.createPlayers(); //TODO: replace
        currentPhase = null;
        board = null;
        cursor = null;
        actionContext = new ActionContext();
        resetActionContext();
    }
    public void resetActionContext() {
        actionContext.graphPath = new DefaultGraphPath<>();
        actionContext.selected = new ArrayList<>();
        actionContext.actions = new ArrayDeque<>();
    }

    public MapObjects createBoard(GameLevel level) {
        MapObjects result = new MapObjects();
        board = new ArrayList<>();
        for (Vector2 position : level.getAccessibleCellPositions()) {
            BoardCellObject cobject = new BoardCellObject((int) position.x, (int) position.y, level.getCellSize());
            cobject.setType(BoardCellObject.CellType.BASIC);
            board.add(cobject);
            result.add(cobject);
        }
        return result;
    }

    public MapObject createCursor(Vector2 cellSize) {
        cursor = new BoardCellObject(0, 0, cellSize);
        cursor.setVisible(true);
        cursor.setName("cursor");
        cursor.setOpacity(0.1f);
        return cursor;
    }


    public Player[] getPlayers() {
        return players;
    }
    public Player currentPlayer() {
        if(players.length == 0) return null;
        return players[_indexCurrentPlayer];
    }

    public List<Fighter> getAllFighters(boolean deadIncluded) {
        List<Fighter> all = new ArrayList<>();
        for (Player player : players) {
            all.addAll( player.getTeam(deadIncluded));
        }
        return all;
    }
    public Fighter getFighter(Vector2 cellCoordinates, boolean deadIncluded) {
        for (Fighter fighter : getAllFighters(deadIncluded)) {
            if (fighter.getPosition().cell().equals(cellCoordinates)) return fighter;
        }
        return null;
    }
    public Fighter getFighter(Coordinates coordinates, boolean deadIncluded) {
        return getFighter(coordinates.cell(), deadIncluded);
    }

    public List<BoardCellObject> getBoard() {
        return board;
    }
    public BoardCellObject getBoardCell(Vector2 cellposition) {
        for (BoardCellObject object : board) {
            if (object.getCellPosition().equals(cellposition)) return object;
        }
        return null;
    }
    public BattleState setBoard(List<BoardCellObject> board) {
        this.board = board;
        return this;
    }

    public ActionContext getActionContext() {
        return actionContext;
    }


    public BoardCellObject getCursor() {
        return cursor;
    }

    public BattleState setCursor(BoardCellObject cursor) {
        this.cursor = cursor;
        return this;
    }

    public BattlePhase getCurrentPhase() {
        return currentPhase;
    }

    public BattleState setCurrentPhase(BattlePhase currentPhase) {
        this.currentPhase = currentPhase;
        return this;
    }
}
