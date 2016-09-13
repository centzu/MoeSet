package com.example.helloworld.moeset.Beans;

import android.graphics.drawable.Drawable;

/**
 * Created by helloworld on 2016/9/6.
 */
public class SongInfo {
    private String name;
    private String url;
    private String artist;
    private String picUrl;
    private String albumName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public SongInfo(){};
    public SongInfo(String name, String url, String artist, String picUrl) {
        this.name = name;
        this.url = url;
        this.artist = artist;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
