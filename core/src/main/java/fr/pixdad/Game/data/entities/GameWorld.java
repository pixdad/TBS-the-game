package fr.pixdad.Game.data.entities;

import java.util.*;

public class GameWorld {
    //TODO: create a builder

    private final String name;
    private final Date creationDate;

    private final List<GameLevel> completeLevels = new ArrayList<>();
    private final List<GameLevel> availableLevels = new ArrayList<>();
    private final List<GameLevel> lockedLevels = new ArrayList<>();

    //TODO: create a stats class
    private final Map<GameLevel, String> levelStat = new HashMap<>();

    public GameWorld(String name) {
        this.name = name;
        this.creationDate = new Date();
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public List<GameLevel> getCompleteLevels() {
        return completeLevels;
    }

    public List<GameLevel> getAvailableLevels() {
        return availableLevels;
    }

    public List<GameLevel> getLockedLevels() {
        return lockedLevels;
    }

    public void completeLevel(GameLevel level) {
        //TODO: level completion
        /*
        0. Check if level is in available -> else throw error ?
        1. move level from available to complete and check for end of game
        2. get into level data the list of next levels to unlock
        3. move the coresponding list of levels from locked to available
         */
    }

}
