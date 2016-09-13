package com.example.helloworld.moeset.Beans.NetEaseResponseBeans;

import java.util.List;

/**
 * Created by helloworld on 2016/9/6.
 */
public class Result {
    private int songCount;
    private List<Song> songs;

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getSongCount() {
        return songCount;
    }

    public List<Song> getSongs() {
        return songs;
    }
    public Song getSong(int postion){
        return songs.get(postion);
    }
}
