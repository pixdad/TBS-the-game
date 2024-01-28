package fr.pixdad.Game.battle.states;

import com.badlogic.gdx.Input;
import fr.pixdad.Game.data.entities.StatsValue;
import fr.pixdad.Game.battle.core.BoardCellObject;
import fr.pixdad.Game.battle.core.Fighter;
import fr.pixdad.Game.battle.core.BattleScreen;

public class TargetSelectionFState extends BattlePhase {

    /**
     * When enter SELECT_TARGET:
     * <ul>
     *     <li>Show the cursor</li>
     *     <li>Color ALLY for the current player fighters</li>
     *     <li>Color SELF for the selected fighter</li>
     *     <li>Color SELECTABLE_ENNEMY for the other player fighters</li>
     *     <li>Populate {@link #modifiedCells} with the colored cells</li>
     * </ul>
     */
    @Override
    public void enter(BattleScreen screen) {
        super.enter(screen);
        screen.getCursor().setVisible(true);

        for (Fighter fighter : screen.getLevel().getAllFighters(false)) {
            BoardCellObject c = screen.getBoardCell(fighter.getPosition().cell());
            if (fighter.player == currentPlayer) {
                c.setType(BoardCellObject.CellType.ALLY);
                if (screen.source == fighter) {
                    c.setType(BoardCellObject.CellType.SELF);
                }
            }
            else {
                c.setType(BoardCellObject.CellType.SELECTABLE_ENNEMY);
            }
            modifiedCells.add(c);
        }

    }

    @Override
    public void update(BattleScreen screen) {

    }

    /**
     * On mouse moved
     * - Search the path between the source and the cursor
     * - show the path
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        super.mouseMoved(screenX, screenY);
        searchPath(5);
        showPath();
        screen.getStatsUI().emptyTargeted();

        Fighter ft = screen.getLevel().getFighter(screen.getCursor().getCellPosition(), false);

        if(ft != null && screen.currentPlayer() == ft.player) {
            //TODO: future handling of healing / support capability
        }
        else if(ft != null && screen.currentPlayer() != ft.player) {
            screen.getStatsUI().showTargeted(ft, Fighter.basicAttakDamages(screen.source.getCurrentStats(), ft.getCurrentStats()), StatsValue.ZERO);
        }

        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //ESCAPE: annuler et revenir à SELECT_PERSO
        if (keycode == Input.Keys.ESCAPE) {
            screen.source = null;
            stateMachine.changeState(FightingScreenState.SELECT_PERSO.get());
            return true;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        BoardCellObject cursor = screen.getCursor();
        cursor.setType(BoardCellObject.CellType.CURSOR);

        //CellMapObject cell = screen.getBoardCell(cursor.getCellPosition());
        if (searchPath(5)) { //TODO: get range dynamically. For now, it is hardcoded at 5
            System.out.println("Cible selectionnée : "+cursor.getCellPosition());
            screen.target = graphPath.get(graphPath.getCount()-1);
            stateMachine.changeState(FightingScreenState.SELECT_ACTION.get());
            return true;
        }
        else {
            //TODO: Afficher erreur
            System.out.println("Out of range");
        }
        return false;
    }
}
