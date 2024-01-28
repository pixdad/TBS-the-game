package fr.pixdad.Game.battle.action;

import fr.pixdad.Game.battle.core.Fighter;
import fr.pixdad.Game.battle.core.BattleScreen;

public class AttackAction extends Action<Fighter, Fighter> {

    //private Fighter _fighterTarget;

    public AttackAction(Fighter source, Fighter target) {
        super(source, target);
    }

    @Override
    public boolean animate(float delta, BattleScreen screen) {

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
