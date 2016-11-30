package com.example.shengdong.hw;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * the player controlled by user
 */
public class Player extends GameObject{
    private Paint paint;
    private Rect rect;
    private boolean playing;
    private int startX;
    private int startY;

    private boolean moving = false;

    private static double acceleration ;
    /**
     * constructor for player
     * @param startX the start x point
     * @param startY the start y point
     * @param length the length of the square
     */
    public Player(int startX, int startY, int length) {
        width = length;
        this.startX = startX;
        this.startY = startY;

        this.x = startX;
        this.y = startY;

        velocity = width;
        acceleration = velocity /3;


        paint = new Paint();
        paint.setColor(Color.RED);

        rect = new Rect(x - width/2, y + width/2, x + width/2, y - width/2 );

    }

    /**
     * set action from user input
     * @param e1 first input event
     * @param e2 second input event
     */
    public void setEvent(MotionEvent e1, MotionEvent e2){
        if(!moving){
            vectorX = e2.getX() - e1.getX();
            vectorY = e2.getY() - e1.getY();
            moving = true;
            if(vectorX == 0)
                vectorX = 0.1f;
            Log.d("Vector", "vx: " + vectorX);
            Log.d("Vector", "vy: " + vectorY);
            Log.d("start", "x: " + x);
            Log.d("start", "y: " + y);
        }
    }
    public void update() {
        if(moving) {
            velocity -= 0.5 * acceleration;
            double delta = velocity;
            updateLocation(delta);
            checkCurrentPosition();

            rect = new Rect(x - width/2, y + width/2, x + width/2, y - width/2 );
        }
    }

    /**
     * check position of player, if the player is moving beyond the original point reset the position
     */
    private void checkCurrentPosition(){

        if((vectorX > 0 & x < startX)|| (vectorY > 0 && y < startY) || (vectorY < 0 && y > startY) || (vectorX < 0 && x > startX) ){
            Log.d("Rest", "done ");
            resetMovement();
        }
    }

    private void resetVelocity() {
        velocity = width;
    }

    /**
     * draw the player square
     * @param canvas canvas of game panel
     */
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}

    private void resetMovement(){
        y = startY;
        x = startX;
        vectorX = 0;
        vectorY = 0;
        moving = false;
        resetVelocity();
    }

    public boolean isMoving() {
        return moving;
    }
}
