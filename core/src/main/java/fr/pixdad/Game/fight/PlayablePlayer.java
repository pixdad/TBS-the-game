package fr.pixdad.Game.fight;

import com.badlogic.gdx.InputProcessor;
import fr.pixdad.Game.data.entities.GameLevel;

public class PlayablePlayer extends Player {

    public PlayablePlayer(String name) {
        super(name);
    }

    @Override
    public Fighter selectSource(GameLevel level) {
        return null;
    }

    @Override
    public Fighter selectTarget(GameLevel level) {
        return null;
    }

    @Override
    public void selectAction(GameLevel level) {

    }

}
