package fr.pixdad.Game.fight.states;

import fr.pixdad.Game.fight.action.Action;

import java.util.ArrayDeque;

public class ActionExecutionFState extends GlobalFState {
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void update(FightingScreen screen) {
        super.update(screen);

        //Execute each action, one after the previous (FIFO list)
        ArrayDeque<Action> actions = screen.actions;
        Action action = actions.peekFirst();

        if(action == null) {
            if (currentPlayer.fightersToAct.contains(screen.source)) {
                screen.target = null;
                stateMachine.changeState(FightingScreenState.SELECT_TARGET.get());
            }
            else {
                screen.source = null;
                screen.target = null;
                stateMachine.changeState(FightingScreenState.SELECT_PERSO.get());
            }
        }
        else {
            if(action.isDone()) actions.removeFirst();
            else action.animate(delta, screen);
        }
    }
}
