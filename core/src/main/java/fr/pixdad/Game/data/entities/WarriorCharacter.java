package fr.pixdad.Game.data.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WarriorCharacter implements Character {

    protected TextureRegion textureRegion;
    protected Texture texture;
    protected String name;
    protected int level = 1;
    protected StatsValue baseStatsValue = new StatsValue(5,5,5,50);

    //TODO: store animations of a character
    //protected Set<String, Animation>



    public WarriorCharacter() {
        Texture texture = new Texture("purple16px.png");
        this.textureRegion = new TextureRegion(texture, 0, 0, 16, 16);
    }

    public WarriorCharacter(String name, StatsValue baseStatsValue) {
        this.name = name;
        this.baseStatsValue = baseStatsValue;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public StatsValue getCurrentModifier() {
        return baseStatsValue.add(new StatsValue(level - 1, level - 1, level - 1, (level-1)*5));
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void levelup() {

    }
}
