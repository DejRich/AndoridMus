package com.example.shengdong.hw;

import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * main activity that shows a list of music from storage for user to choose
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Music> musicList;
    private ListView  musicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicList = new ArrayList<>();
        musicView = (ListView)findViewById(R.id.music_list);

        setMusicList();
        setMusicView();

    }

    /**
     * set music list by query the device storage
     * cited from https://code.tutsplus.com/tutorials/create-a-music-player-on-android-project-setup--mobile-22764
     */
    public void setMusicList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor != null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                musicList.add(new Music(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

    /**
     * set music view by setting adapter
     */
    public void setMusicView(){
        MusicAdapter adapter = new MusicAdapter(this, musicList);
        musicView.setAdapter(adapter);
    }

    /**
     * start a new activity and pass the music uri to new activity
     * @param view the one called the method
     */
    public void chooseMusic(View view){
        MusicAdapter.ViewHolder holder = (MusicAdapter.ViewHolder) view.getTag();
        int position = holder.getPosition();
        Music currMusic = musicList.get(position);
        long currMusicId = currMusic.getID();
        Uri musicUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currMusicId);


        Toast.makeText(this,"Processing music file", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, PlayMusic.class);
        intent.setData(musicUri);
        intent.putExtra("title", currMusic.getTitle());
        startActivity(intent);

    }

}
