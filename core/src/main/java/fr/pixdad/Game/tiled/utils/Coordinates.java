package fr.pixdad.Game.tiled.utils;

import fr.pixdad.Game.fight.states.FightingScreen;
import fr.pixdad.Game.tiled.maps.TiledScreen;
import com.badlogic.gdx.math.Vector2;

public class Coordinates {
    private Vector2 windowCoordinates;
    private Vector2 worldCoordinates;
    private Vector2 cellCoordinates;

    //True if window & world coordinates are synchronised with cell coordinates
    private Boolean isSynchonized;

    public Coordinates() {
        windowCoordinates = new Vector2();
        worldCoordinates = new Vector2();
        cellCoordinates = new Vector2(0,0);

        //Init default values
        setCellCoordinates(Vector2.Zero);
        isSynchonized = false;
    }

    public Coordinates(FightingScreen screen) {
        windowCoordinates = new Vector2();
        worldCoordinates = new Vector2();
        cellCoordinates = new Vector2(0,0);

        //Init default values
        setCellCoordinates(Vector2.Zero, screen);
        isSynchonized = true;
    }
    public void setWindowCoordinates(Vector2 windowCoordinates, TiledScreen screen) {
        this.windowCoordinates.set(windowCoordinates);
        this.worldCoordinates.set(screen.window2world(windowCoordinates));
        this.cellCoordinates.set(screen.window2cell(windowCoordinates));
        this.isSynchonized = true;
    }

    public void setWorldCoordinates(Vector2 worldCoordinates, TiledScreen screen) {
        this.windowCoordinates.set(screen.world2window(worldCoordinates));
        this.worldCoordinates.set(worldCoordinates);
        this.cellCoordinates.set(screen.world2cell(worldCoordinates));
        this.isSynchonized = true;
    }

    public void setCellCoordinates(Vector2 cellCoordinates, TiledScreen screen) {
        this.windowCoordinates.set(screen.cell2window(cellCoordinates));
        this.worldCoordinates.set(screen.cell2world(cellCoordinates));
        this.cellCoordinates.set(cellCoordinates);
        this.isSynchonized = true;
    }

    public void setCellCoordinates(Vector2 cellCoordinates) {
        this.cellCoordinates.set(cellCoordinates);
        this.isSynchonized = false;
    }

    public void synchronize(FightingScreen screen) {
        if (!isSynchonized) {
            setCellCoordinates(this.cellCoordinates, screen);
        }
    }
    public Vector2 window(FightingScreen screen) {
        synchronize(screen);
        return windowCoordinates;
    }
    public Vector2 world(FightingScreen screen) {
        synchronize(screen);
        return worldCoordinates;
    }
    public Vector2 cell() {
        return cellCoordinates;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Coordinates c = (Coordinates) obj;
        return c.cellCoordinates.equals(this.cellCoordinates);
    }

    public static Coordinates createCoordinateFromCell(FightingScreen screen, Vector2 cellCoordinates) {
        Coordinates c = new Coordinates(screen);
        c.setCellCoordinates(cellCoordinates);
        return c;
    }
}
