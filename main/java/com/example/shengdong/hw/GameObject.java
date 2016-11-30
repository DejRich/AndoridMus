package com.example.shengdong.hw;

import android.graphics.Rect;


public class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected float vectorX;
    protected float vectorY;

    protected double velocity;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public Rect getRectangle() {
        return new Rect(x - width/2, y - width/2, x + width/2, y + width/2 );
    }

    /**
     * update the x, y by delta
     * @param delta the delta distance of movement
     */
    protected void updateLocation(double delta) {
        double rad = Math.atan(Math.abs(vectorY/vectorX));
        double deltaX = Math.cos(rad) * delta;
        double deltaY = Math.sin(rad) * delta;
        if(vectorX < 0)
            deltaX = -deltaX;
        if(vectorY < 0)
            deltaY = -deltaY;
        x += deltaX;
        y += deltaY;
    }

    public float getVectorX() {
        return vectorX;
    }

    public float getVectorY() {
        return vectorY;
    }
}
