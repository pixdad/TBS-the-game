package fr.pixdad.Game.tiled.utils;

public class Margin {
    public float LEFT = 0f;
    public float TOP = 0f;
    public float RIGHT = 0f;
    public float BOTTOM = 0f;

    public Margin() {
    }

    public Margin(float LEFT, float TOP, float RIGHT, float BOTTOM) {
        this.LEFT = LEFT;
        this.TOP = TOP;
        this.RIGHT = RIGHT;
        this.BOTTOM = BOTTOM;
    }

    public float[] getArray() {
        float[] arr = {LEFT, TOP, RIGHT, BOTTOM};
        return arr;
    }
}
