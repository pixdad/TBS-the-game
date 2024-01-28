package fr.pixdad.Game.battle.core;

import fr.pixdad.Game.data.entities.Character;
import fr.pixdad.Game.data.entities.Equipment;
import fr.pixdad.Game.data.entities.StatsValue;
import fr.pixdad.Game.tiled.utils.Coordinates;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent a character involved in a fight (with equipment, position, etc.)
 */
public class Fighter {

    private final Coordinates _position;
    //private TextureMapObject _textObj;

    //Base character class
    public Character character;

    public Boolean isVisible = true;

    public Player player;

    public Equipment[] inventory;
    public Map<Equipment.TYPE, Equipment> equiped = new HashMap<>();

    public boolean _hasMoved = false;
    public boolean _hasAttacked = false;
    public int movementPoints = 4;
    public boolean _isDead = false;
    public int kills = 0;
    public int causedDamages = 0;

    public int hp = 0;
    StatsValue baseStats = new StatsValue(
            10,
            1,
            1,
            20
    );
    ArrayList<StatsValue> modifiers = new ArrayList<>();


    /*
    public int atk = 10;
    public int str = 1;
    public int def = 1;
    public int hp = 20;
    public int hpMax = 20;
    */


    //INITIALISER, GETTERS AND SETTERS
    public Fighter(Character character, Player player) {
        this.character = character;
        this.player = player;
        this.hp = baseStats.hpMax;

        _position = new Coordinates();
        //_textObj = new TextureMapObject(character.getTextureRegion());

//        _textObj.setOriginX(screen.world2window(new Vector2(16, 16)).x);
        //_updateTextObjectPosition();
    }

    //We update both the position of the fighter & its TextureMapObject
    public void setPosition(Coordinates position, BattleScreen screen) {
        this._position.setCellCoordinates(position.cell());
    }
    public Coordinates getPosition() {
        return _position;
    }

    public void setCellCoordinates(Vector2 cellCoordinates) {
        _position.setCellCoordinates(cellCoordinates);
    }

    public StatsValue getBaseStats() {
        return this.baseStats;
    }
    public StatsValue getCurrentModifier() {
        StatsValue current = StatsValue.ZERO;
        for(StatsValue modif : modifiers) {
            current = current.add(modif);
        }
        return current;
    }
    public StatsValue getCurrentStats() {
        return baseStats.add(getCurrentModifier());
    }

    public void fighterIsDone() {
        player.fighterHasActed(this);
    }
    public void die() {
        this._isDead = true;
    }
    public boolean isDead() {return _isDead; }

    public void attack(Fighter ennemy) {
        int damages = basicAttakDamages(this.getCurrentStats(), ennemy.getCurrentStats());
        this.causedDamages += damages;
        int newHp = ennemy.hp - damages;
        if (newHp <= 0) {
            newHp = 0;
            ennemy.die();
            kills++;
        }
        ennemy.hp = newHp;
    }


    public static int basicAttakDamages(StatsValue src, StatsValue tgt) {
        int hpDamages = ( src.atk * (10 + src.str) - tgt.def ) /10;
        return Math.max(hpDamages, 1);
    }
}
