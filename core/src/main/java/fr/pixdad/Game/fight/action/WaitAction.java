package fr.pixdad.Game.fight.action;

import fr.pixdad.Game.fight.states.FightingScreen;

public class WaitAction extends Action {

    private float _deltaStart = 0;
    private float _seconds;

    public WaitAction(float seconds) {
        super(null, null);
        this._seconds = seconds;
    }

    @Override
    public boolean animate(float delta, FightingScreen screen) {
        if(_deltaStart == 0) _deltaStart = delta;
        float elsapsed = delta - _deltaStart;

        if (elsapsed >= _seconds) {
            _isDone = true;
        }

        return true;
    }
}
