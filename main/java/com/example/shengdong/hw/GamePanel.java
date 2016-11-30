package com.example.shengdong.hw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;

/**
 * The surface view used for the game
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Player player;
    private Uri musicUri;
    MediaPlayer mediaPlayer = null;
    boolean isRunning = false;
    private ArrayList<Bullet> bullets;
    private Random rand = new Random();
    private int score;
    private String musicPath;
    private ArrayList<Double> beatsList;
    private int beatsListIndex = 0;

    public GamePanel(Context context){
        super(context);
    }

    public GamePanel(Context context, Uri musicUri, String musicPath) {
        super(context);

        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        this.musicUri = musicUri;
        this.musicPath =  musicPath;

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        player = new Player(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 8);
        bullets = new ArrayList<>();


        //set ffmpeg
        new AndroidFFMPEGLocator(this.getContext());
        PercussionDetector pDetector = new PercussionDetector(musicPath);

        beatsList = pDetector.getBeatsList();
        removeOverlapBeats();

        thread.setRunning(true);
        thread.start();
        score = 0;
    }

    public void startGame() {
        if(!isRunning){
            isRunning = true;
            try {
                setMediaPlayer();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mediaPlayer.stop();
            thread.setRunning(false);
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * get events from activity and set play action
     * @param e1 first event of fling
     * @param e2 second event of fling
     */
    public void setPlayerEvent(MotionEvent e1, MotionEvent e2) {
        player.setEvent(e1, e2);
    }

    /**
     * update game objects
     */
    public void update() {
        //set timer and calculate delta time
        double currentTime = 0, deltaTime = 1200;
        if(beatsListIndex < beatsList.size()){
            currentTime = (double)mediaPlayer.getCurrentPosition();
            deltaTime = beatsList.get(beatsListIndex) - currentTime;
        }
            //background.update(); update background if needed
        //add a bullet if the timing the right
        if(deltaTime < 1200 ){
            beatsListIndex++;
            int bx, by;
            double distance = getWidth()/2 * getWidth()/2 + getHeight()/2 * getHeight()/2;
            bx = rand.nextInt(getWidth());
            int tx = getWidth()/2 - bx;
            by = (int)Math.sqrt(distance - (tx * tx) );
            if(rand.nextBoolean())
                by = getHeight()/2 + by;
            else
                by = getHeight()/2 - by;
            bullets.add(new Bullet(bx, by, getWidth()/2, getHeight()/2 , player.getWidth()/2));
        }
        //update all the bullets location
        for(int i = 0; i< bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            if(collision(player, b) ) {
                score -= 100;
                bullets.remove(i);
            }
            else if(outBound(b)){
                score += 100;
                bullets.remove(i);
            }
        }
        //update player location
        player.update();
    }

    /**
     * draw all the game objects
     * @param canvas the canvas from thread
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRGB(0,0,0);
        for(Bullet b : bullets)
            b.draw(canvas);
        player.draw(canvas);

        if(!isRunning){
            //game has not started, show start screen
            drawStart(canvas);
        }
        else if(!mediaPlayer.isPlaying()){
            //music finished, show end screen
            drawEnd(canvas);
        }
        else{
            //game is running, show scores
            Paint tPaint = new Paint();
            tPaint.setColor(Color.GRAY);
            tPaint.setTextSize(100);
            tPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score:" + score,100,100,tPaint);
        }

    }

    /**
     * draw the end canvas
     * @param canvas the canvas
     */
    public void drawEnd(Canvas canvas){
        Paint tPaint = new Paint();
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(100);
        tPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Finished! Your Score: "+ score, this.getWidth() / 2, 2 * this.getHeight() / 3, tPaint);
    }

    /**
     * draw the start canvas
     * @param canvas canvas
     */
    public void drawStart(Canvas canvas){
        Paint tPaint = new Paint();
        tPaint.setColor(Color.RED);
        tPaint.setTextSize(100);
        tPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Click to start", this.getWidth() / 2, 2 * this.getHeight() /3 , tPaint);
    }

    /**
     * set the media player
     * @throws IOException
     */
    public void setMediaPlayer() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getContext(), musicUri);
        mediaPlayer.prepare();
    }

    /**
     *  check collision of game obeject
     * @param a game object a
     * @param b game object b
     * @return true if a and b collide
     */
    public boolean collision(GameObject a, GameObject b){
        return Rect.intersects(a.getRectangle(),b.getRectangle());
    }

    /**
     * check if the bullet is out of screen bound
     * @param b the bullet object
     * @return true if bullet is out bound
     */
    private boolean outBound(Bullet b) {
        int bx = b.getX();
        int by = b.getY();
        return (by < 0 || by > getHeight()) && (bx < 0 || bx > getWidth());
    }

    /**
     * remove beats that are too close for player to react
     */
    private void removeOverlapBeats(){
        for(int i=0; i <beatsList.size() - 1;i++){
            double temp = beatsList.get(i);
            if(beatsList.get(i+1)- temp < 500)
                beatsList.remove(i+1);
        }
    }
}
