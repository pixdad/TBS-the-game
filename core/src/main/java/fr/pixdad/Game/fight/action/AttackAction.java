package fr.pixdad.Game.fight.action;

import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.fight.states.FightingScreen;

public class AttackAction extends Action<Fighter, Fighter> {

    //private Fighter _fighterTarget;

    public AttackAction(Fighter source, Fighter target) {
        super(source, target);
    }

    @Override
    public boolean animate(float delta, FightingScreen screen) {

        if( !isInit()) {
            //TODO: Trow error
            done();
            return false;
        }

        if( !isDone() ) {

            _source.attack(_target);
            if ( !_target.isDead() ) _target.attack(_source);

            done();
            _source.fighterIsDone();
        }

        return true;
    }
}
