package com.example.shengdong.hw;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class mockTest {

    @Mock
    Context MockContext;
    @Mock
    MainThread thread;
    @Mock
    MotionEvent e1;
    @Mock
    MotionEvent e2;

    @Test
    /**
     * test player's react to inputs
     */
   public void test_player_action(){
        Player player = new Player(100, 100, 10);

        assertFalse(player.isMoving());

        //set start point
        when(e1.getX()).thenReturn((float)100);
        when(e1.getY()).thenReturn((float)100);

        //fling
        when(e2.getX()).thenReturn((float)200);
        when(e2.getY()).thenReturn((float)200);

        player.setEvent(e1, e2);
        assertTrue(player.isMoving());
        assertTrue(player.getVectorX() > 0);
        assertTrue(player.getVectorY() > 0);

        //update 12 times so player should move back to original point
        for(int i =0; i < 12; i++){
            player.update();
        }
        assertFalse(player.isMoving());
        assertEquals(100, player.getX());
        assertEquals(100, player.getY());

    }

}
