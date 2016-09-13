package com.example.helloworld.moeset.Beans.NetEaseResponseBeans;

/**
 * Created by helloworld on 2016/9/6.
 */
public class Album {
    private int id;
    private String name;
    private String picUrl;
    private Artist artist;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
