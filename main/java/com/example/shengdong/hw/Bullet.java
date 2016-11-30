package com.example.shengdong.hw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Bullet extends GameObject {
    private Rect rect;
    private Paint paint;

    public Bullet(int x, int y, int centerX, int centerY, int width){
        this.x = x;
        this.y = y;
        this.vectorX = centerX - x;
        if(vectorX == 0)
            vectorX = 0.1f;
        this.vectorY = centerY - y;
        this.width = width;
        this.velocity = 2*width/3;

        paint = new Paint();
        paint.setColor(Color.BLUE);

        rect = new Rect(x - width/2, y + width/2, x + width/2, y - width/2 );
    }

    public void update() {
        double delta = velocity;
        updateLocation(delta);

        rect = new Rect(x - width/2, y + width/2, x + width/2, y - width/2 );
    }



    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
