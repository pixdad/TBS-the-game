package fr.pixdad.Game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.pixdad.Game.battle.core.PlayablePlayer;
import fr.pixdad.Game.battle.core.Player;
import fr.pixdad.Game.data.entities.Character;
import fr.pixdad.Game.data.entities.StatsValue;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.data.entities.WarriorCharacter;
import fr.pixdad.Game.battle.core.Fighter;
import fr.pixdad.Game.battle.core.BoardCellObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * Class storing the initial game data. The data should not be modified during the course of the game.
 * Game state data and saves will be handled by different class
 * TODO: add here in the doc the reference to the future game data class created.
 */
public class DataManagerOld {

    private static final String DATAPATH = "data.json";

    private static JsonValue gameData;


    public static final HashMap<BoardCellObject.CellType, Texture> cellMapObjectTextures = new HashMap<BoardCellObject.CellType, Texture>();
    public static final HashMap<String, Texture> customTextures = new HashMap<String, Texture>();
    public static final HashMap<String, Character> characters = new HashMap<String, Character>();
    public static final HashMap<String, GameLevel> levels = new HashMap<String, GameLevel>();

    public static void loadData() {
        FileHandle handle = Gdx.files.internal(DATAPATH);
        String jsonString = handle.readString();
        gameData = new JsonReader().parse(jsonString);

        JsonValue mapsData = gameData.get("maps");
        JsonValue heroesData = gameData.get("heroes");

        loadLevels(mapsData);
        loadHeroes(heroesData);
    }

    private static void loadLevels(JsonValue mapsData) {
        JsonValue.JsonIterator it = mapsData.iterator();
        while (it.hasNext()) {
            JsonValue map = it.next();
            levels.put(map.getString("name"), createGameLevel(map));
        }
    }
    private static GameLevel createGameLevel(JsonValue mapData) {
        GameLevel level = new GameLevel(
                mapData.getString("name"),
                mapData.getString("filename")
        );

        JsonValue.JsonIterator posIt = mapData.get("teamInitPosition").iterator();
        while (posIt.hasNext()) {
            JsonValue pos = posIt.next();
            level.addPosition(new Vector2(pos.getInt("x"), pos.getInt("y")));
        }
        return level;
    }

    private static void loadHeroes(JsonValue heroesData) {
        JsonValue.JsonIterator it = heroesData.iterator();
        while (it.hasNext()) {
            JsonValue hero = it.next();
            characters.put(hero.getString("id"), createCharacter(hero));
        }
    }


    private static Character createCharacter(JsonValue heroData) {
        switch (heroData.getString("type")) {
            case "warrior":

                StatsValue stat = new StatsValue(
                        heroData.get("baseProperties").getInt("atk"),
                        heroData.get("baseProperties").getInt("str"),
                        heroData.get("baseProperties").getInt("def"),
                        heroData.get("baseProperties").getInt("hpMax")
                );

                Character warrior = new WarriorCharacter(heroData.getString("name"), stat);

                //TODO: init animation data of character created
                //Animation

                return warrior;

            default:
                throw new IllegalArgumentException("type of hero not existing");

        }
    }

    public static Player[] createPlayers() {

        Player play1 = new PlayablePlayer("Player 1");
        Player play2 = new PlayablePlayer("Player 2");

        WarriorCharacter warrior = new WarriorCharacter();

        Fighter fight1 = new Fighter(warrior, play1);
        Fighter fight2 = new Fighter(warrior, play2);

        fight1.setCellCoordinates(new Vector2(5, 5));
        fight2.setCellCoordinates(new Vector2(3,3));
        System.out.println(fight2.getPosition().cell());
        System.out.println(Vector2.X);

        play1.addTeamMember(fight1);
        play2.addTeamMember(fight2);

        play1.initTurn();
        play2.initTurn();

        return new Player[]{play1, play2};
    }

    public static void initTextures() {

        //Life bar
        Pixmap pixmap = new Pixmap(16, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0,0, 16, 4);
        customTextures.put("life bar foreground", new Texture(pixmap));
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0,0, 16, 4);
        customTextures.put("life bar background", new Texture(pixmap));
        pixmap.dispose();



        //CellMapObjects textures
        for (BoardCellObject.CellType cellType : BoardCellObject.CellType.values()) {
            Color fillColor = null;
            Color lineColor = Color.BLACK;
            switch (cellType) {
                case CURSOR:
                    lineColor = Color.GOLD;
                    break;
                case BASIC:
                    lineColor = new Color(0,0,0,0.5f);
                    break;
                case SELECTABLE_ALLY:
                    //lineColor = new Color(0,0,1, 1f);
                    fillColor = new Color(0,0,1,1f);
                    break;
                case SELECTABLE_ENNEMY:
                    //lineColor = new Color(1,0,0,1f);
                    fillColor = new Color(1,0,0,1f);
                    break;
                case SELECTABLE_MOVE:
                    fillColor = new Color(0,1,0,1f);
                    break;
                case ALLY:
                    fillColor = new Color(0,0,1,0.7f);
                    break;
                case ENNEMY:
                    fillColor = new Color(1,0,0,0.7f);
                    break;
                case PATH:
                    fillColor = new Color(1,1,1,1f);
                    break;
                case SELF:
                    fillColor = Color.CYAN;
            }

            Pixmap pixmap1 = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
            if (fillColor != null) {
                pixmap1.setColor(fillColor);
                pixmap1.fillRectangle(0,0, 32, 32);
            }
            if (lineColor != null) {
                pixmap1.setColor(lineColor);
                pixmap1.drawRectangle(0,0, 32, 32);
            }

            Texture texture = new Texture(pixmap1);
            pixmap1.dispose();
            cellMapObjectTextures.put(cellType, texture);
        }
    }

    public static Texture getCellMapObjectTexture(BoardCellObject.CellType type) {
        return cellMapObjectTextures.get(type);
    }
    public static Texture getCustomTexture(String key) {
        return customTextures.get(key);
    }



}
