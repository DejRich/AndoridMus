package com.example.shengdong.hw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter used to connect view and data
 */

public class MusicAdapter extends BaseAdapter {
    private ArrayList<Music> musicList;
    private LayoutInflater musicInf;

    public MusicAdapter(Context c, ArrayList<Music> musicList) {
        this.musicList = musicList;
        this.musicInf = LayoutInflater.from(c);
    }

    /**
     * set the view by getting the data from music list
     * @param position the position of the data
     * @param convertView current view
     * @param parent the parent of current view
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if(convertView == null){
            vi = musicInf.inflate(R.layout.music, parent, false);
            holder = new ViewHolder();
            holder.musicTitle = (TextView) vi.findViewById(R.id.title);
            holder.musicArtist = (TextView) vi.findViewById(R.id.artist);
        }
        else
            holder = (ViewHolder) vi.getTag();
        Music curr = musicList.get(position);
        holder.musicTitle.setText(curr.getTitle());
        holder.musicArtist.setText(curr.getArtist());
        holder.position = position;
        vi.setTag(holder);
        return vi;
    }
    @Override
    public int getCount() {
        return musicList.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * view holder for view holder pattern
     */
    public class ViewHolder {
        public TextView musicTitle;
        public TextView musicArtist;
        public int position;

        public int getPosition() {
            return position;
        }
    }

}
