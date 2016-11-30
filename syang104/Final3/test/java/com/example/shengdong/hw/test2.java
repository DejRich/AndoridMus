package com.example.shengdong.hw;

import android.graphics.Paint;
import android.graphics.Rect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;

import java.util.ArrayList;

public class test2 {

    @Test
    public void testBullet(){
        ArrayList<Bullet>bullets = new ArrayList<Bullet>();
        bullets.add(new Bullet(0, 0, 50, 50 ,15));
        Rect center = new Rect(45,45,55,55);
        for(int i =0; i < 4; i++){
            bullets.get(0).update();
        }
        assertFalse(Rect.intersects( bullets.get(0).getRectangle(),center));
        bullets.get(0).update();
        assertTrue(Rect.intersects( bullets.get(0).getRectangle(),center));
    }
}
