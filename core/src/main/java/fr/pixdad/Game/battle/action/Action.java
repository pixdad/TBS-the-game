package fr.pixdad.Game.battle.action;

import fr.pixdad.Game.battle.core.BattleScreen;

public abstract class Action<U,V> {

    /*
    Actions possibles :
    - MoveAction
    - AttacAction
    - HealAction
    - UseObjectAction
    - (EndTurn ?)
    - EquipAction
     */

    protected U _source;
    protected V _target;

    protected boolean _isDone = false;

    public abstract boolean animate(float delta, BattleScreen screen);

    public Action(U source, V target) {
        _source = source;
        _target = target;
    }

    public boolean isDone() {
        return _isDone;
    }
    public void done() { _isDone = true; }
    public void setSource(U source) {
        this._source = source;
    }
    public U getSource() {
        return this._source;
    }
    public void setTarget(V target) {
        this._target = target;
    }
    public V getTarget() { return this._target; }

    public boolean isInit() {
        return (this._source != null && this._target != null);
    }

}
