package fr.pixdad.Game.battle.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import fr.pixdad.Game.data.DataManagerOld;

public class BoardCellObject extends TextureMapObject {

    public enum CellType {
        BASIC,
        ALLY,
        ENNEMY,
        SELECTABLE_MOVE,
        SELECTABLE_ALLY,
        SELECTABLE_ENNEMY,
        SELF,
        PATH,
        CURSOR
    }

    protected Vector2 cellPosition;
    protected Vector2 cellSize;
    protected CellType type = CellType.BASIC;
    protected CellType previousType = CellType.BASIC;

    public BoardCellObject(Vector2 cellSize) {
        this(0, 0, cellSize);
    }

    public BoardCellObject(int col, int row, Vector2 cellSize) {
        //super(col*cellSize.x, row*cellSize.y, cellSize.x, cellSize.y);
        super();
        this.cellSize = cellSize.cpy();
        this.cellPosition = new Vector2(col, row);
        setX(col*cellSize.x);
        setY(row*cellSize.y);

        TextureRegion tr = new TextureRegion(DataManagerOld.getCellMapObjectTexture(type));
        setTextureRegion(tr);

        setScaleX(cellSize.x / tr.getRegionWidth());
        setScaleY(cellSize.y / tr.getRegionHeight());

    }

    public Vector2 getCellPosition() {
        cellPosition.x = (int) (getX() / cellSize.x) ;
        cellPosition.y = (int) (getY() / cellSize.y) ;
        return cellPosition;
    }

    public void setCellPosition(Vector2 cellPosition) {
        this.cellPosition = cellPosition.cpy();
        Vector2 position = cellPosition.cpy().scl(cellSize);

        setX(position.x);
        setY(position.y);
    }


    public CellType getType() {
        return type;
    }
    public void setType(CellType type) {
        this.previousType = this.type;
        this.type = type;
        setTextureRegion(new TextureRegion(DataManagerOld.getCellMapObjectTexture(type)));
    }
    public void revertType() {
        CellType temp = type;
        setType(previousType);
        previousType = temp;
    }
}
