package com.example.helloworld.moeset.Beans.NetEaseResponseBeans;

import java.util.List;

/**
 * Created by helloworld on 2016/9/6.
 */
public class Song {
    private int id;
    private String name;
    private List<Artist> artists;
    private Album album;
    private String audio;
    private int djProgramId;

    public String getAudio() {
        return audio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist(int i) {
        return artists.get(i);
    }
}
