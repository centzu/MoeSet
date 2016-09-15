package com.example.helloworld.moeset.engine;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by helloworld on 2016/9/7.
 */

public class MusicPlayer implements MediaPlayer.OnPreparedListener {
    public MediaPlayer mMediaPlayer;
    private String mMusicUrl = " ";
    private String mPlayingMusicUrl = "  ";

    public MusicPlayer() {
        initData();
    }
//"/sdcard/netease/cloudmusic/Music/凑诗 - 圣者遗物.mp3";

    public MusicPlayer(String mMusicUrl) {
        setMusicUrl(mMusicUrl);
        initData();
    }

    public String getmPlayingMusicUrl() {
        return mPlayingMusicUrl;
    }

    public void setmPlayingMusicUrl(String mPlayingMusicUrl) {
        this.mPlayingMusicUrl = mPlayingMusicUrl;
    }

    public String getMusicUrl() {
        return mMusicUrl;
    }

    public void setMusicUrl(String mMusicUrl) {
        this.mMusicUrl = mMusicUrl;
    }

    private void initData() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);

    }

    public void playMusic(String url) throws IOException {
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepare();
    }

    public void playMusic() throws IOException {
        mMediaPlayer.reset();

        mMediaPlayer.setDataSource(mMusicUrl);
        Log.i("PLAYMUSIC", "playMusic: "+mMusicUrl);
        mPlayingMusicUrl = mMusicUrl;
        Log.i("player", "playMusic: prepare start~~~");
        mMediaPlayer.prepare();
        Log.i("player", "playMusic: prepare completed~~~");


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    public void start() {
        mMediaPlayer.start();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void stopAndrelease() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }


}
