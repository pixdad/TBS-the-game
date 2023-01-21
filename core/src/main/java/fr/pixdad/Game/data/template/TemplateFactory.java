package fr.pixdad.Game.data.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class TemplateFactory {

    private static final Map<String, Class<? extends DataTemplate>> TEMPLATES_TYPES = new HashMap<>();
    static {
        TEMPLATES_TYPES.put("levelTemplate", LevelTemplate.class);
        TEMPLATES_TYPES.put("worldTemplate", WorldTemplate.class);
    }

    public static boolean isValidTemplateType(String templateType) {
        return TEMPLATES_TYPES.containsKey(templateType);
    }

    public static DataTemplate createTemplateFromPath(String path) throws Exception {
        FileHandle handle = Gdx.files.internal(path);
        return createTemplateFromFileHandler(handle);
    }
    public static DataTemplate createTemplateFromFileHandler(FileHandle fileHandle) throws Exception {
        String jsonString = fileHandle.readString();
        JsonValue templateContent =  new JsonReader().parse(jsonString);
        String templateType = templateContent.getString("templateType", "null");
        if( isValidTemplateType(templateType)) {
            return TEMPLATES_TYPES.get(templateType).getDeclaredConstructor(JsonValue.class).newInstance(templateContent);
        }
        else
            throw new Exception("Template not found matching templateType property: " + templateContent.getString("templateType", "null"));
    }
}
