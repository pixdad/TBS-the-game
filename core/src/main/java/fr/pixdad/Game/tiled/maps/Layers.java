package fr.pixdad.Game.tiled.maps;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Layers {
    public final TiledMapTileLayer tilesGround;
    public final TiledMapTileLayer tilesOnGround;
    public final MapLayer cells;
    public final MapLayer fighters;
    public final MapLayer flyers;
    public final MapLayer ui;

    public Layers(TiledMapTileLayer TILES_GROUND, TiledMapTileLayer TILES_ON_GROUND, MapLayer CELLS, MapLayer FIGHTERS, MapLayer FLYERS, MapLayer UI) {
        this.tilesGround = TILES_GROUND;
        this.tilesOnGround = TILES_ON_GROUND;
        this.cells = CELLS;
        this.fighters = FIGHTERS;
        this.flyers = FLYERS;
        this.ui = UI;
    }
}
