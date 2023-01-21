package fr.pixdad.Game.tiled.utils;

import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import fr.pixdad.Game.fight.Player;
import fr.pixdad.Game.fight.states.FightingScreen;

public class TiledPathFinder {

    public static class TiledGraph implements IndexedGraph<CellMapObject> {

        final FightingScreen screen;

        public CellMapObject to;
        public CellMapObject from;

        public TiledGraph(FightingScreen screen) {
            this.screen = screen;
        }

        public CellMapObject to() {
            return to;
        }

        public TiledGraph to(CellMapObject to) {
            this.to = to;
            return this;
        }

        public CellMapObject from() {
            return from;
        }

        public TiledGraph from(CellMapObject from) {
            this.from = from;
            return this;
        }

        private Boolean isPassageOnAllyAllowed = true;
        private Boolean isPassageOnEnemyAllowed = false;

        private Boolean isEndOnAllyAllowed = false;
        private Boolean isEndOnEnemyAllowed = true;

        @Override
        public int getIndex(CellMapObject node) {
            return getIndex(node, true, false, false, true);
        }

        public int getIndex(CellMapObject node,
                            Boolean isPassageOnAllyAllowed, Boolean isPassageOnEnemyAllowed, Boolean isEndOnAllyAllowed, Boolean isEndOnEnemyAllowed) {
            this.isEndOnAllyAllowed = isEndOnAllyAllowed;
            this.isEndOnEnemyAllowed = isEndOnEnemyAllowed;
            this.isPassageOnAllyAllowed = isPassageOnAllyAllowed;
            this.isPassageOnEnemyAllowed = isPassageOnEnemyAllowed;
            return screen.getBoard().indexOf(node);
        }

        @Override
        public int getNodeCount() {
            return screen.getBoard().size();
        }

        @Override
        public Array<Connection<CellMapObject>> getConnections(CellMapObject fromNode) {
            Vector2 position = fromNode.getCellPosition();
            Array<Connection<CellMapObject>> connections = new Array<>();

            CellMapObject[] surrounds = {
                    screen.getBoardCell(new Vector2(position.x - 1, position.y)),
                    screen.getBoardCell(new Vector2(position.x + 1, position.y)),
                    screen.getBoardCell(new Vector2(position.x, position.y - 1)),
                    screen.getBoardCell(new Vector2(position.x, position.y + 1)),
            };

            for (CellMapObject surround : surrounds) {
                if(surround == null) continue;
                else if(screen.getLevel().getFighter(surround.getCellPosition(), false) == null) {
                    connections.add(new DefaultConnection<>(fromNode, surround));
                }
                else {
                    Player playerForFighter = screen.getLevel().getFighter(surround.getCellPosition(), false).player;
                    Player currentPlayer = screen.currentPlayer();

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

    final TiledGraph graph;
    final IndexedAStarPathFinder<CellMapObject> pathFinder;
    final Heuristic<CellMapObject> heuristic = new Heuristic<>() {
        @Override
        public float estimate(CellMapObject node, CellMapObject endNode) {
            if (node == null || endNode == null) return -1f;
            return node.getCellPosition().dst2(endNode.getCellPosition());
        }
    };

    public TiledPathFinder(final FightingScreen screen) {
        this.graph = new TiledGraph(screen);
        this.pathFinder = new IndexedAStarPathFinder<CellMapObject>(graph);
    }

    public boolean searchConnectionPath(CellMapObject startNode, CellMapObject endNode, GraphPath<Connection<CellMapObject>> outPath) {
        if (startNode == null || endNode == null) return false;
        graph.from(startNode).to(endNode);
        return pathFinder.searchConnectionPath(startNode, endNode, heuristic, outPath);
    }

    public boolean searchNodePath(CellMapObject startNode, CellMapObject endNode, GraphPath<CellMapObject> outPath) {
        if (startNode == null || endNode == null) return false;
        graph.from(startNode).to(endNode);
        return pathFinder.searchNodePath(startNode, endNode, heuristic, outPath);
    }

    /*public boolean search(PathFinderRequest<CellMapObject> request, long timeToRun) {
        return pathFinder.search(request, timeToRun);
    }*/
}
