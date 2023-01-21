package fr.pixdad.Game.data.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Character {

    TextureRegion getTextureRegion();
    void setTextureRegion(TextureRegion textureRegion);
    String getName();
    void setName(String name);
    StatsValue getCurrentModifier();
    void setLevel(int level);
    void levelup();
}
