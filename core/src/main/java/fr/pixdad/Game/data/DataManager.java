package fr.pixdad.Game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.data.entities.GameWorld;
import fr.pixdad.Game.data.template.DataTemplate;
import fr.pixdad.Game.data.template.LevelTemplate;
import fr.pixdad.Game.data.template.TemplateFactory;
import fr.pixdad.Game.data.template.WorldTemplate;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static final String TEMPLATE_FOLDER_PATH = "templates";

    private static DataManager _instance = null;
    public static DataManager get() {
        if(_instance == null)  _instance = new DataManager();
        return _instance;
    }


    private Map<String, LevelTemplate> levelTemplates;
    private WorldTemplate worldTemplate;
    private GameWorld currentWorld;

    private DataManager() {
        levelTemplates = new HashMap<>();
    }

    private void loadTemplates(String folderPath, Boolean isRecursive) {
        FileHandle handle = Gdx.files.internal(folderPath);
        for (FileHandle fh : handle.list()) {
            if(fh.isDirectory() && isRecursive) {
                loadTemplates(fh.path(), true);
            }
            else if (!fh.isDirectory() && fh.extension().equals("json")) {
                try {
                    DataTemplate template = TemplateFactory.createTemplateFromFileHandler(fh);
                    registerTemplate(template);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void registerTemplate(DataTemplate template) {
        switch (template.templateType) {
            case "levelTemplate":
                levelTemplates.put(template.UID, (LevelTemplate) template);
                break;
            case "worldTemplate":
                worldTemplate = (WorldTemplate) template;
                break;
            default:
                break;
        }
    }

    public void init() {
        loadTemplates(TEMPLATE_FOLDER_PATH, true);
    }

    public GameWorld createNewWorld(String name) {
        if(worldTemplate == null) return null;

        GameWorld world = new GameWorld(name);

        JsonValue worldLevels = worldTemplate.templateContent.get("levels");
        worldLevels.forEach(worldLevel -> {
            LevelTemplate levelTemplate = this.levelTemplates.get(worldLevel.getString("levelUID"));
            GameLevel gameLevel = createGameLevel(levelTemplate);
            if (worldLevel.getBoolean("startLevel", false)) {
                world.getAvailableLevels().add(gameLevel);
            }
            else {
                world.getLockedLevels().add(gameLevel);
            }
        });

        setCurrentWorld(world);
        return world;
    }

    public GameLevel createGameLevel(LevelTemplate template) {
        return new GameLevel(template.UID, template.getMapString());
    }

    public GameWorld getCurrentWorld() {
        return currentWorld;
    }

    public void setCurrentWorld(GameWorld currentWorld) {
        this.currentWorld = currentWorld;
    }
}
