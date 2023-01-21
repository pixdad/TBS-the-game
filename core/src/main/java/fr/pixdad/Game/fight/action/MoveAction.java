package fr.pixdad.Game.fight.action;

import fr.pixdad.Game.fight.Fighter;
import fr.pixdad.Game.fight.states.FightingScreen;
import fr.pixdad.Game.tiled.utils.CellMapObject;
import fr.pixdad.Game.tiled.utils.Coordinates;

public class MoveAction extends Action<Fighter, CellMapObject> {

    public MoveAction(Fighter source, CellMapObject target) {
        super(source, target);
    }

    @Override
    public boolean animate(float delta, FightingScreen screen) {
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
