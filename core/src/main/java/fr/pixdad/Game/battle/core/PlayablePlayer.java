package fr.pixdad.Game.battle.core;

import fr.pixdad.Game.data.entities.GameLevel;

public class PlayablePlayer extends Player {

    public PlayablePlayer(String name) {
        super(name);
    }

    @Override
    public Fighter getSource(GameLevel level) {
        return null;
    }

    @Override
    public Fighter computeTarget(GameLevel level) {
        return null;
    }

    @Override
    public void computeAction(GameLevel level) {

    }

}
