package fr.pixdad.Game.fight.states;

import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.data.entities.StatsValue;
import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.tiled.utils.CellMapObject;

public class PersoSelectionFState extends GlobalFState {

    /**
     * On enter:
     * - show cursor
     * - end the turn if no more selectable fighters
     * - Marks the ALLY, the SELECTABLE_ALLYs, and the ENNEMYs (and populate {@link #modifiedCells} with them)
     */
    @Override
    public void enter(FightingScreen screen) {
        super.enter(screen);
        screen.getStatsUI().empty();
        screen.getCursor().setVisible(true);

        //automatic end of turn
        //TODO:handle success
        if (currentPlayer.fightersToAct.size() <= 0) {
            screen.moveToNextPlayer();
            return;
        }

        for (Fighter fighter : screen.getLevel().getAllFighters(false)) {
            CellMapObject c = screen.getBoardCell(fighter.getPosition().cell());
            if (fighter.player == currentPlayer) {
                c.setType(CellMapObject.CellType.ALLY);
                if (currentPlayer.fightersToAct.contains(fighter)) c.setType(CellMapObject.CellType.SELECTABLE_ALLY);
            }
            else {
                c.setType(CellMapObject.CellType.ENNEMY);
            }
            modifiedCells.add(c);
        }
    }

    @Override
    public void update(FightingScreen screen) {
        Fighter source = screen.currentPlayer().selectSource(screen.level);
        if(source != null) {
            screen.source = source;
            screen.stateMachine.changeState(FightingScreenState.SELECT_TARGET.get());
        }
    }

    /**
     * When mouse moved, show the cursor if it is in the board, hide it otherwise
     * TODO: Show hints/stats when over entities
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean result = true;
        super.mouseMoved(screenX, screenY);
        CellMapObject cursor = screen.getCursor();
        cursor.setType(CellMapObject.CellType.CURSOR);
        cursor.setVisible(true);

        screen.getStatsUI().empty();

        Fighter fighter = screen.getLevel().getFighter(cursor.getCellPosition(), false);
        if (fighter != null) {
            if (fighter.player == screen.currentPlayer()) screen.getStatsUI().showSelected(fighter);
            else screen.getStatsUI().showTargeted(fighter, 0, StatsValue.ZERO);
        }

        if (screen.getBoardCell(cursor.getCellPosition()) == null) {
            cursor.setVisible(false);
            result = false;
        }
        return result;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * When click on an entity:
     * - select as source and switch to the FightingScreenState.SELECT_TARGET state if owned by current player
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Vector2 cellPosition = screen.window2cell(new Vector2(screenX, screenY));

        Fighter fighter = screen.getLevel().getFighter(cellPosition, false);
        if (fighter == null) {}
        else if (currentPlayer.fightersToAct.contains(fighter)) {
            screen.source = fighter;
            screen.stateMachine.changeState(FightingScreenState.SELECT_TARGET.get());
            return true;
        } else {
            //TODO: Display error (try to select ennemy / fighter already acted), or stats, etc.
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }
}
