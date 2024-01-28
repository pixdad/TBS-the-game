package fr.pixdad.Game.tiled.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import fr.pixdad.Game.TBSGame;
import fr.pixdad.Game.tiled.utils.Margin;


/**
 * Created by David on 17/11/2016.
 * Abscract Screen class for tiled screens. Render entities between layers
 */
public abstract class TiledScreen implements Screen {


//region Attributes
/* =========================================================== */

    String TAG = TiledScreen.class.getSimpleName();

    protected final TBSGame game;

    protected final ExtendedRenderer renderer;
    protected final OrthographicCamera camera;

    protected Vector2 screenSizePx;
    protected Vector2 worldSizePX;
    protected Vector2 viewportSizePX;
    //protected Vector2 cellCountSize;
    protected Vector2 cellSize;

    protected final Margin cameraBounds = new Margin();
    protected final Margin gutters = new Margin();
    protected Vector3 cameraPosition;
    protected boolean moveCameraToLeft = false;
    protected boolean moveCameraToTop = false;
    protected boolean moveCameraToRight = false;
    protected boolean moveCameraToBottom = false;
    protected float cameraSpeed = 100;

    protected float zoom = 1;
    public float getZoom() { return zoom; }
    /* =========================================================== */
//endregion



//region Methods
/* =========================================================== */

    /**
     *
     * @param game
     * @param map
     * @param groundLayer bottom map layer, to compute map size
     * @param zoom Multiplier used to scale the world size
     */
    public TiledScreen(TBSGame game, TiledMap map, TiledMapTileLayer groundLayer, float zoom) {
        this.game = game;
        this.zoom = zoom;

        screenSizePx = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        cellSize = new Vector2(groundLayer.getTileWidth(), groundLayer.getTileWidth() );

        Vector2 cellCountSize = new Vector2(groundLayer.getWidth(), groundLayer.getHeight() );
        worldSizePX = cellCountSize.cpy().scl(cellSize);

        Gdx.app.log(TAG, "Window size in px: "+screenSizePx);
        Gdx.app.log(TAG, "World size in px: "+worldSizePX);
        Gdx.app.log(TAG, "Cell size in px: "+cellSize);

        //Need of a custom renderer in order to render the objects
        renderer = new ExtendedRenderer(this, map);//, this.scale);
        //camera=new OrthographicCamera(worldSizePX.x, worldSizePX.y);
        camera=new OrthographicCamera();
        camera.zoom = this.zoom;

        updateViewport();
    }

    public void updateViewport() {
        Rectangle viewport = new Rectangle(0,0,screenSizePx.x, screenSizePx.y);
        //viewport.fitInside(new Rectangle(0,0,worldSizePX.x, worldSizePX.y));
        viewport.fitOutside(new Rectangle(0,0,10*cellSize.x, 10*cellSize.y));
        viewportSizePX = new Vector2(viewport.width, viewport.height);
        camera.setToOrtho(false, viewport.width, viewport.height);

        cameraPosition = camera.position;

        //Boundaries for the camera : left, top, right, bottom
        float horizontalMargin = (viewport.width*camera.zoom-worldSizePX.x>0) ? (viewport.width*camera.zoom-worldSizePX.x)/2 : 0;
        float verticalMargin = (viewport.height*camera.zoom-worldSizePX.y>0) ? (viewport.height*camera.zoom-worldSizePX.y)/2 : 0;
        cameraBounds.LEFT = viewport.width*camera.zoom/2 - horizontalMargin;
        cameraBounds.TOP = worldSizePX.y - viewport.height*camera.zoom/2 + verticalMargin;
        cameraBounds.RIGHT = worldSizePX.x - viewport.width*camera.zoom/2 + horizontalMargin;
        cameraBounds.BOTTOM = viewport.height*camera.zoom/2 - verticalMargin;
    }

    /* Utils methods */

    public Vector2 window2world(Vector2 windowCoord) {
        Vector3 u = new Vector3(windowCoord, 0);
        Vector3 v = camera.unproject(u);
        return new Vector2(v.x, v.y);
    }
    public Vector2 world2window(Vector2 worldCoord) {
        Vector3 u = new Vector3(worldCoord, 0);
        Vector3 v = camera.project(u);
        return new Vector2(v.x, v.y);
    }
    public Vector2 world2cell(Vector2 worldCoord) {
        return new Vector2((int) (worldCoord.x/ cellSize.x), (int) (worldCoord.y/ cellSize.y));
    }
    public Vector2 cell2world(Vector2 cellCoord) {
        return cellCoord.cpy().scl(cellSize);
    }
    public Vector2 cell2window(Vector2 cellCoord) {
        return world2window(cell2world(cellCoord));
    }
    public Vector2 window2cell(Vector2 windowCoord) {
        return world2cell(window2world(windowCoord));
    }

    public float dXworld2window(float dXworld) {
        return dXworld * screenSizePx.x / viewportSizePX.x / camera.zoom;
    }
    public float dYworld2window(float dYworld) {
        return dYworld * screenSizePx.y / viewportSizePX.y / camera.zoom;
    }
    public float dXwindow2world(float dXwindow) {
        return dXwindow * viewportSizePX.x * camera.zoom / screenSizePx.x;
    }
    public float dYwindow2world(float dYwindow) {
        return dYwindow * viewportSizePX.y * camera.zoom / screenSizePx.y;
    }

    protected void drawRectangleMapObject(RectangleMapObject robject, Batch batch) {
        /*Rectangle rect = robject.getRectangle();
        Color color = robject.getColor();
        color.a = robject.getOpacity();
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(color);

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(rect.x, rect.y, rect.width, rect.height);
        sr.end();
        batch.begin();*/
    }

    public void addInLayer(MapLayer layer, MapObject object) {
        layer.getObjects().add(object);
    }
    public void addInLayer(MapLayer layer, MapObjects objects) {
        for (MapObject obj : objects) {
            addInLayer(layer, obj);
        }
    }


    public void removeFromLayer(MapLayer layer, MapObject object) {
        layer.getObjects().remove(object);
    }
    public void removeFromLayer(MapLayer layer, MapObjects objects) {
        for (MapObject object : objects) {
            removeFromLayer(layer, object);
        }
    }


    public void renderCamera(float delta) {

        //Translation
        if(moveCameraToLeft) cameraPosition.x -= cameraSpeed*delta;
        if(moveCameraToTop) cameraPosition.y += cameraSpeed*delta;
        if(moveCameraToRight) cameraPosition.x += cameraSpeed*delta;
        if(moveCameraToBottom) cameraPosition.y -= cameraSpeed*delta;

        // Position compared to cameraBounds
        if (cameraPosition.x < cameraBounds.LEFT - gutters.LEFT) cameraPosition.x = cameraBounds.LEFT - gutters.LEFT; //left
        if (cameraPosition.y > cameraBounds.TOP - gutters.TOP) cameraPosition.y = cameraBounds.TOP - gutters.TOP; //top
        if (cameraPosition.x > cameraBounds.RIGHT - gutters.RIGHT) cameraPosition.x = cameraBounds.RIGHT - gutters.RIGHT; //right
        if (cameraPosition.y < cameraBounds.BOTTOM - gutters.BOTTOM) cameraPosition.y = cameraBounds.BOTTOM - gutters.BOTTOM; //bottom

        //Update camera position
        camera.position.set(cameraPosition);

        //Clear screan
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        //update camera & refresh view
        camera.update();
        renderer.setView(camera);
    }

    public void renderMap() {
        renderer.render();
    }

    public SpriteBatch getBatch() {return game.batch;}

    public Margin getGutters() {
        return gutters;
    }
    public float getCameraSpeed() {
        return cameraSpeed;
    }
    public void setCameraSpeed(float cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
    }
    /* =========================================================== */
//endregion



//region Screen methods
/* =========================================================== */


    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {
        this.screenSizePx.set(width, height);
        updateViewport();
    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {

    }


    /* =========================================================== */
//endregion
}

