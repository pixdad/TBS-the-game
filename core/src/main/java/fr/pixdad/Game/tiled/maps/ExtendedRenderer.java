package fr.pixdad.Game.tiled.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class ExtendedRenderer extends OrthogonalTiledMapRenderer {
    private final TiledScreen tiledScreen;

    public ExtendedRenderer(TiledScreen tiledScreen, TiledMap map) {
        super(map);
        this.tiledScreen = tiledScreen;
    }

    public ExtendedRenderer(TiledScreen tiledScreen, TiledMap map, Batch batch) {
        super(map, batch);
        this.tiledScreen = tiledScreen;
    }

    public ExtendedRenderer(TiledScreen tiledScreen, TiledMap map, float unitScale) {
        super(map, unitScale);
        this.tiledScreen = tiledScreen;
    }

    public ExtendedRenderer(TiledScreen tiledScreen, TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
        this.tiledScreen = tiledScreen;
    }

    @Override
    public void renderObject(MapObject object) {
        if (object.isVisible()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject robject = (RectangleMapObject) object;
                tiledScreen.drawRectangleMapObject(robject, batch);
            } else if (object instanceof TextureMapObject) {
                TextureMapObject textObj = (TextureMapObject) object;
                TextureRegion texRegion = textObj.getTextureRegion();
                batch.draw(texRegion
                        , textObj.getX()
                        , textObj.getY()
                        , textObj.getOriginX()
                        , textObj.getOriginY()
                        , texRegion.getRegionWidth()
                        , texRegion.getRegionHeight()
                        , textObj.getScaleX()
                        , textObj.getScaleY()
                        , textObj.getRotation()
                );

            } else {
                super.renderObject(object);
            }
        }
    }
}
