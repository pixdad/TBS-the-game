package fr.pixdad.Game.data.template;

import com.badlogic.gdx.utils.JsonValue;

public abstract class DataTemplate {

    public final JsonValue templateContent;
    public final String templateType;
    public final String UID;
    public final String name;
    public final String desc;

    public DataTemplate(JsonValue templateContent) {
        this.templateContent = templateContent;
        this.templateType = templateContent.getString("templateType", "dataTemplate");
        this.UID = templateContent.getString("UID");
        this.name = templateContent.getString("name");
        this.desc = templateContent.getString("desc");
    }

//    public static Boolean validateSchema(String modelPath, String schemaPath) {
//        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
//        JsonSchema schema = factory.getSchema(Gdx.files.internal(schemaPath).readString());
//        return false;
//    }
}
