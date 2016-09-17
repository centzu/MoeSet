package com.example.helloworld.moeset.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.helloworld.moeset.engine.MusicPlayer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicPlayerService extends Service {
    private String url;
    private MusicPlayer mMusicPlayer = null;
    private static PlayMusicThread mPlayMusicThread;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private boolean isStartThread = false;
    private Exception exception = null;

    public MusicPlayerService() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Exception getException() {
        return exception;
    }

    public MusicPlayer getMusicPlayer() {
        if (mMusicPlayer == null && url != null) {
            mMusicPlayer = new MusicPlayer(url);
        } else if (mMusicPlayer == null) {
            mMusicPlayer = new MusicPlayer();
        }
        return mMusicPlayer;
    }

    public void setMusicPlayer(MusicPlayer mMusicPlayer) {
        this.mMusicPlayer = mMusicPlayer;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("sysout", "onBind: running");
        setUrl(intent.getStringExtra("MusicUrl"));
        mMusicPlayer = getMusicPlayer();
        mPlayMusicThread = new PlayMusicThread(mPlayMusicThread,mMusicPlayer);
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log.i("musicplayer", "onStartCommand: running");
        return super.onStartCommand(intent, flags, startId);
    }

    public void PlayMusic() {
        exception = null;
        mMusicPlayer.setMusicUrl(url);
        singleThreadExecutor.execute(mPlayMusicThread);
    }

    @Override
    public void onDestroy() {
        if(mMusicPlayer!=null)
            mMusicPlayer.stopAndrelease();
        super.onDestroy();

    }

    public class MusicPlayerBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    private MusicPlayerBinder myBinder = new MusicPlayerBinder();

    private class PlayMusicThread extends Thread implements Runnable {
        private MusicPlayer MusicPlayer;

        public PlayMusicThread(Runnable runnable, com.example.helloworld.moeset.engine.MusicPlayer musicPlayer) {
            super(runnable);
            MusicPlayer=musicPlayer;

        }
        @Override
        public void run() {
            try {
                MusicPlayer.playMusic();
            } catch (IOException e) {
                exception=e;
                e.printStackTrace();
            }
        }
    }
}
