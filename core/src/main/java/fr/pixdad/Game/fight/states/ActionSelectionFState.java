package fr.pixdad.Game.fight.states;

import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.fight.action.AttackAction;
import fr.pixdad.Game.fight.action.MoveAction;

public class ActionSelectionFState extends GlobalFState{
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void enter(FightingScreen screen) {
        graphPath.clear();
        super.enter(screen);
    }

    @Override
    public void update(FightingScreen screen) {
        super.update(screen);
        screen.pathfinder.searchNodePath(screen.getBoardCell(screen.source.getPosition().cell()), screen.target, graphPath);

        Fighter targetFighter = screen.getLevel().getFighter(screen.target.getCellPosition(), false);
        if (targetFighter == null) {
            MoveAction maction = new MoveAction(screen.source, screen.target);
            screen.actions.offerLast(maction);
        }
        else if(targetFighter.player != screen.source.player) {
            MoveAction maction = new MoveAction(screen.source, graphPath.get(graphPath.getCount()-2));
            AttackAction aaction = new AttackAction(screen.source, targetFighter);
            screen.actions.offerLast(maction);
            screen.actions.offerLast(aaction);
        }
        else if(targetFighter == screen.source) {
            //TODO: Self action
        }
        else {
            //TODO: Heal action
        }


        stateMachine.changeState(FightingScreenState.COMPUTE_ACTION.get());
    }
}
