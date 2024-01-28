package fr.pixdad.Game.battle.action;

import fr.pixdad.Game.battle.core.BoardCellObject;
import fr.pixdad.Game.battle.core.Fighter;
import fr.pixdad.Game.battle.core.BattleScreen;
import fr.pixdad.Game.tiled.utils.Coordinates;

public class MoveAction extends Action<Fighter, BoardCellObject> {

    public MoveAction(Fighter source, BoardCellObject target) {
        super(source, target);
    }

    @Override
    public boolean animate(float delta, BattleScreen screen) {
        if ( !isInit() ) {
            _isDone = true;
            return false;
        }
        if ( !isDone() ) {
            if (_source == null) {
                System.out.println("Rien à déplacer (pas de fighter)");
                _isDone = true;
                return true;
            }

            Coordinates coordinates = Coordinates.createCoordinateFromCell(screen, _target.getCellPosition());
            _source.setPosition(coordinates, screen);
            _source.fighterIsDone();
            _isDone = true;

        }
        return true;
    }
}
