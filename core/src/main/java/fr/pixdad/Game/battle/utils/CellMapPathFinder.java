package fr.pixdad.Game.battle.utils;

import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import fr.pixdad.Game.battle.core.BattleState;
import fr.pixdad.Game.battle.core.BoardCellObject;
import fr.pixdad.Game.battle.core.Player;
import fr.pixdad.Game.battle.core.BattleScreen;
import fr.pixdad.Game.data.entities.GameLevel;

public class CellMapPathFinder {

    public static class CellMapGraph implements IndexedGraph<BoardCellObject> {

        BattleState battleState;
        public BoardCellObject to;
        public BoardCellObject from;

        public CellMapGraph(BattleState battleState) {
            this.battleState = battleState;
        }

        public BoardCellObject to() {
            return to;
        }

        public CellMapGraph to(BoardCellObject to) {
            this.to = to;
            return this;
        }

        public BoardCellObject from() {
            return from;
        }

        public CellMapGraph from(BoardCellObject from) {
            this.from = from;
            return this;
        }

        private Boolean isPassageOnAllyAllowed = true;
        private Boolean isPassageOnEnemyAllowed = false;

        private Boolean isEndOnAllyAllowed = false;
        private Boolean isEndOnEnemyAllowed = true;

        @Override
        public int getIndex(BoardCellObject node) {
            return getIndex(node, true, false, false, true);
        }

        public int getIndex(BoardCellObject node,
                            Boolean isPassageOnAllyAllowed, Boolean isPassageOnEnemyAllowed, Boolean isEndOnAllyAllowed, Boolean isEndOnEnemyAllowed) {
            this.isEndOnAllyAllowed = isEndOnAllyAllowed;
            this.isEndOnEnemyAllowed = isEndOnEnemyAllowed;
            this.isPassageOnAllyAllowed = isPassageOnAllyAllowed;
            this.isPassageOnEnemyAllowed = isPassageOnEnemyAllowed;
            return battleState.getBoard().indexOf(node);
        }

        @Override
        public int getNodeCount() {
            return battleState.getBoard().size();
        }

        @Override
        public Array<Connection<BoardCellObject>> getConnections(BoardCellObject fromNode) {
            Vector2 position = fromNode.getCellPosition();
            Array<Connection<BoardCellObject>> connections = new Array<>();

            BoardCellObject[] surrounds = {
                    battleState.getBoardCell(new Vector2(position.x - 1, position.y)),
                    battleState.getBoardCell(new Vector2(position.x + 1, position.y)),
                    battleState.getBoardCell(new Vector2(position.x, position.y - 1)),
                    battleState.getBoardCell(new Vector2(position.x, position.y + 1)),
            };

            for (BoardCellObject surround : surrounds) {
                if(surround == null) continue;
                else if(battleState.getFighter(surround.getCellPosition(), false) == null) {
                    connections.add(new DefaultConnection<>(fromNode, surround));
                }
                else {
                    Player playerForFighter = battleState.getFighter(surround.getCellPosition(), false).player;
                    Player currentPlayer = battleState.currentPlayer();

                    //List accept conditions
                    if(     surround == this.to() && playerForFighter == currentPlayer && isEndOnAllyAllowed ||
                            surround == this.to() && playerForFighter != currentPlayer && isEndOnEnemyAllowed ||
                            surround != this.to() && playerForFighter == currentPlayer && isPassageOnAllyAllowed ||
                            surround != this.to() && playerForFighter != currentPlayer && isPassageOnEnemyAllowed
                    ) {
                        connections.add(new DefaultConnection<>(fromNode, surround));
                    }
                }
            }
            return connections;
        }

    }

    final CellMapGraph graph;
    final IndexedAStarPathFinder<BoardCellObject> pathFinder;
    final Heuristic<BoardCellObject> heuristic = new Heuristic<>() {
        @Override
        public float estimate(BoardCellObject node, BoardCellObject endNode) {
            if (node == null || endNode == null) return -1f;
            return node.getCellPosition().dst2(endNode.getCellPosition());
        }
    };

    public CellMapPathFinder(final BattleState battleState) {
        this.graph = new CellMapGraph(battleState);
        this.pathFinder = new IndexedAStarPathFinder<BoardCellObject>(graph);
    }

    public boolean searchConnectionPath(BoardCellObject startNode, BoardCellObject endNode, GraphPath<Connection<BoardCellObject>> outPath) {
        if (startNode == null || endNode == null) return false;
        graph.from(startNode).to(endNode);
        return pathFinder.searchConnectionPath(startNode, endNode, heuristic, outPath);
    }

    public boolean searchNodePath(BoardCellObject startNode, BoardCellObject endNode, GraphPath<BoardCellObject> outPath) {
        if (startNode == null || endNode == null) return false;
        graph.from(startNode).to(endNode);
        return pathFinder.searchNodePath(startNode, endNode, heuristic, outPath);
    }

    /*public boolean search(PathFinderRequest<CellMapObject> request, long timeToRun) {
        return pathFinder.search(request, timeToRun);
    }*/
}
