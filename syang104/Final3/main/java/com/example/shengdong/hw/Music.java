package com.example.shengdong.hw;

/**
 * The music class that hold data of a music
 */
public class Music {
    private long id;
    private String title;
    private String artist;

    public Music(long id, String title, String artist){
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public long getID(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
}
