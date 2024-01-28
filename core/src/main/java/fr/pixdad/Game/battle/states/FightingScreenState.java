package fr.pixdad.Game.battle.states;


public enum FightingScreenState  {

    ASSIGN_POSIITONS(new BattlePhase() {
        @Override
        public boolean scrolled(float v, float v1) {
            return false;
        }
    }),
    SELECT_PERSO(new PersoSelectionFState()),
    SELECT_TARGET(new TargetSelectionFState()) ,
    SELECT_ACTION(new ActionSelectionFState()) ,
    COMPUTE_ACTION(new ActionExecutionFState()) ,
    ;

    private BattlePhase _state;

    FightingScreenState(BattlePhase state) {
        this._state = state;
    }
    public BattlePhase get() {
        return _state;
    }
}
