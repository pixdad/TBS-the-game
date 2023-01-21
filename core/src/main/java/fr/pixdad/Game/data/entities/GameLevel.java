package fr.pixdad.Game.data.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.data.DataManagerOld;
import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.fight.Player;
import fr.pixdad.Game.fight.states.FightingScreen;
import fr.pixdad.Game.tiled.maps.Layers;
import fr.pixdad.Game.tiled.utils.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameLevel {

    private final String mapTmxPath;
    private final String name;
    private final Set<Vector2> teamPositions = new HashSet<>();

    protected final TiledMap map;
    protected final Layers layers;
    protected final Vector2 levelSize;

    protected Player[] players;

    private Boolean isAtScreen = false;
    private FightingScreen screenAttached;

    public GameLevel(String name, String mapTmxPath) {
        this.mapTmxPath = mapTmxPath;
        this.name = name;
        this.map = new TmxMapLoader().load(this.mapTmxPath);

        layers = new Layers(
                (TiledMapTileLayer) map.getLayers().get("tilesGround"),
                (TiledMapTileLayer) map.getLayers().get("tilesOnGround"),
                map.getLayers().get("cells"),
                map.getLayers().get("fighters"),
                map.getLayers().get("flyers"),
                map.getLayers().get("ui")
        );

        levelSize = new Vector2(layers.tilesGround.getWidth(), layers.tilesGround.getHeight() );

        this.players = DataManagerOld.createPlayers();

    }

    /*public void attachScreen(FightingScreen screen) {
        this.screenAttached = screen;
        this.isAtScreen = true;
    }*/

    //GETTERS & SETTERS


    public Vector2 getSize() {
        return levelSize;
    }

    public TiledMap getMap() { return map; }

    public void addPosition(Vector2 position) {
        teamPositions.add(position);
    }

    public String getMapTmxPath() {
        return mapTmxPath;
    }

    public String getName() {
        return name;
    }

    public Set<Vector2> getTeamPositions() {
        return teamPositions;
    }

    public Layers getLayers() { return layers; }

    public Player[] getPlayers() { return players; }

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


    public ArrayList<Coordinates> getIntersectCoordinates(ArrayList<Coordinates> first, ArrayList<Coordinates> second) {
        ArrayList<Coordinates> intersect = new ArrayList<Coordinates>();
        for (Coordinates coord : first) {
            if (second.contains(coord)) intersect.add(coord);
        }
        return intersect;
    }

    public boolean isCellPositionAccessible(int col, int row) {

        String s_groundPassable = (layers.tilesGround.getCell(col, row) != null
                && layers.tilesGround.getCell(col, row).getTile().getProperties().containsKey("passable")) ?
                layers.tilesGround.getCell(col, row).getTile().getProperties().get("passable", String.class) :
                null;
        String s_onGroundPassable = (layers.tilesOnGround.getCell(col, row) != null
                && layers.tilesOnGround.getCell(col, row).getTile().getProperties().containsKey("passable")) ?
                layers.tilesOnGround.getCell(col, row).getTile().getProperties().get("passable", String.class) :
                null;

        //layers.tilesOnGround

        int gp = (s_groundPassable != null) ? Integer.parseInt(s_groundPassable) : -1;
        int ogp = (s_onGroundPassable != null) ? Integer.parseInt(s_onGroundPassable) : -1;

        return (ogp != -1) ? ogp == 1 : gp == 1;
    }

    //TODO: implements success logic
    public boolean checkSuccessConditions() {
        return false;
    }
    //TODO: implements fail logic
    public boolean checkFailureConditions() {
        return false;
    }


}
