package fr.pixdad.Game.data.template;

import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class WorldTemplate extends DataTemplate {

    protected final List<JsonValue> levels = new ArrayList<>();

    public WorldTemplate(JsonValue templateContent) {
        super(templateContent);
        templateContent.get("levels").forEach(x -> levels.add(x));

    }
}
