package fr.pixdad.Game.fight.states;


public enum FightingScreenState  {

    SELECT_PERSO(new PersoSelectionFState()),
    SELECT_TARGET(new TargetSelectionFState()) ,
    SELECT_ACTION(new ActionSelectionFState()) ,
    COMPUTE_ACTION(new ActionExecutionFState()) ,
    ;

    private GlobalFState _state;

    FightingScreenState(GlobalFState state) {
        this._state = state;
    }
    public GlobalFState get() {
        return _state;
    }
}
