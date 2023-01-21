package fr.pixdad.Game.data.template;

import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class LevelTemplate extends DataTemplate {

    protected final List<JsonValue> globalEvents = new ArrayList<>();

    public LevelTemplate(JsonValue template) {
        super(template);
        for(JsonValue event : templateContent.get("globalEvents")) {
            globalEvents.add(event);
        }
    }

    public String getMapString() {
        return templateContent.getString("map");
    }
    public List<JsonValue> getGlobalEvents() {
        return globalEvents;
    }
}
