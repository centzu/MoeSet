package com.example.helloworld.moeset.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.helloworld.moeset.R;
import com.example.helloworld.moeset.Service.MusicPlayerService;
import com.example.helloworld.moeset.engine.MusicDownloadManager;
import com.example.helloworld.moeset.engine.MusicPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by helloworld on 2016/9/7.
 */
public class PlayMusicActivity extends Activity implements MediaPlayer.OnCompletionListener {
    static final public int MUSIC_PLAY_ING = 1;
    static final public int MUSIC_PLAY_PAUSE = 0;
    private TextView mtvPlayMusicName;
    private TextView mtvPlayMusicArtist;
    private Button mbtPlayMusic;
    private int MusicState = 0;
    private String mMusicUrl;
    private SeekBar msbMusicPlayProcess;
    private MusicPlayer mMusicPlayer;
    private int mMusicProcess;
    private int mSeekBarProcess = 0;
    private Timer mTimer = new Timer();
    private boolean isBinder = false;
    private MusicPlayerService mMusicPlayerService;
    private Intent intent;
    private boolean isFirst = false;
    private CircularProgressButton mBtnDownloadMusic;
    private Long MydownloadID;
    private MusicDownloadManager musicDownloadManager;
    private MusicDownloadCompleteReceiver MDCReceiver;
    private boolean isLoading=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        initView();
        //mbtPlayMusic.performClick();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("sysout", "onServiceConnected: running");
            MusicPlayerService.MusicPlayerBinder musicPlayerBinder = (MusicPlayerService.MusicPlayerBinder) service;
            mMusicPlayerService = musicPlayerBinder.getService();
            mMusicPlayerService.setUrl(mMusicUrl);
            mMusicPlayer = mMusicPlayerService.getMusicPlayer();
            mMusicPlayer.mMediaPlayer.setOnCompletionListener(PlayMusicActivity.this);
            if (mMusicPlayerService.getUrl().equals(mMusicPlayer.getmPlayingMusicUrl())) {
                if (mMusicPlayer.mMediaPlayer.isPlaying()) {
                    mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    MusicState = MUSIC_PLAY_ING;
                } else {
                    mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    MusicState = MUSIC_PLAY_PAUSE;
                }

                handler.sendEmptyMessage(0);
            }
            mTimer.schedule(timerTask, 0, 1000);
            mMusicPlayer.mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (mMusicPlayerService != null && mMusicPlayer != null)
                        if (mMusicPlayerService.getUrl().equals(mMusicPlayer.getmPlayingMusicUrl())) {
                            msbMusicPlayProcess.setSecondaryProgress(percent);
                        }
                }
            });
            msbMusicPlayProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    mSeekBarProcess = progress;
                    mMusicProcess = progress * mMusicPlayer.mMediaPlayer.getDuration() / seekBar.getMax();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mMusicPlayer.mMediaPlayer.seekTo(mMusicProcess);

                }
            });

            mbtPlayMusic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MusicState = (MusicState == MUSIC_PLAY_PAUSE) ? MUSIC_PLAY_ING : MUSIC_PLAY_PAUSE;
                    if (MusicState == MUSIC_PLAY_PAUSE) {
                        mMusicPlayer.pause();
                        mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    } else {
                        mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                        if (mSeekBarProcess > 0 && mMusicPlayerService.getUrl().equals(mMusicPlayer.getmPlayingMusicUrl())) {
                            mMusicPlayer.start();
                        } else {
                            mMusicPlayerService.PlayMusic();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Exception e = mMusicPlayerService.getException();
                                    if (e != null) {
                                        Toast.makeText(PlayMusicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        MusicState = MUSIC_PLAY_PAUSE;
                                        mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                                    }
                                }
                            }, 4000);
                        }
                    }
                }
            });
            mBtnDownloadMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLoading)
                        return;
                    String name = getIntent().getStringExtra("MusicName");
                    String artist = getIntent().getStringExtra("MusicArtist");
                    String url = getIntent().getStringExtra("MusicUrl");
                    musicDownloadManager = new MusicDownloadManager(PlayMusicActivity.this, name + "-" + artist+".mp3", url);
                    mBtnDownloadMusic.setProgress(50);
                    IntentFilter filter=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    registerReceiver(MDCReceiver=new MusicDownloadCompleteReceiver(),filter);
                    isLoading=true;
                }
            });
            isBinder = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initView() {
        mbtPlayMusic = (Button) findViewById(R.id.bt_play_music);
        mBtnDownloadMusic = (CircularProgressButton) findViewById(R.id.btn_download_music);
        mBtnDownloadMusic.setProgress(0);
        mBtnDownloadMusic.setIndeterminateProgressMode(true);
        msbMusicPlayProcess = (SeekBar) findViewById(R.id.sb_play_music);
        mtvPlayMusicArtist = (TextView) findViewById(R.id.tv_play_music_artist);
        mtvPlayMusicName = (TextView) findViewById(R.id.tv_play_music_name);
        mtvPlayMusicName.setText(getIntent().getStringExtra("MusicName"));
        mtvPlayMusicArtist.setText(getIntent().getStringExtra("MusicArtist"));
        mMusicUrl = getIntent().getStringExtra("MusicUrl");
        intent = new Intent(PlayMusicActivity.this, MusicPlayerService.class);
        intent.putExtra("MusicUrl", mMusicUrl);
        //http://data.5sing.kgimg.com/G071/M02/18/15/J5QEAFdYAxyAB-GvAJihkXz66UU164.mp3
        if (!isServiceWorked()) {
            startService(intent);
            isFirst = true;
        } else {
            isFirst = false;
        }
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mMusicPlayer.mMediaPlayer == null) {
                return;
            }
            if (mMusicPlayer.mMediaPlayer.isPlaying() && msbMusicPlayProcess.isPressed() == false) {
                handler.sendEmptyMessage(0);
            }

        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currPos = mMusicPlayer.mMediaPlayer.getCurrentPosition();
            int duration = mMusicPlayer.mMediaPlayer.getDuration();
            if (duration > 0 && mMusicPlayerService.getUrl().equals(mMusicPlayer.getmPlayingMusicUrl())) {
                long pos = msbMusicPlayProcess.getMax() * currPos / duration;
                msbMusicPlayProcess.setProgress((int) pos);
            }

        }
    };

    private boolean isServiceWorked() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = new ArrayList<ActivityManager.RunningServiceInfo>();
        for (ActivityManager.RunningServiceInfo rs : runningServiceInfos) {
            if (rs.service.getClassName().toString().equals("MusicPlayerService")) {
                return true;
            }
        }
        return false;
    }

    private void unBind() {
        if (isBinder) {
            unbindService(conn);
            isBinder = false;
            mMusicPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        Log.i("sysout", "onStop: PMA stop");
        unBind();
        if(MDCReceiver!=null)
            unregisterReceiver(MDCReceiver);
        mTimer.cancel();
        super.onStop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isFinishing()) {
            mSeekBarProcess = 0;
            MusicState = 0;
            msbMusicPlayProcess.setProgress(0);
            mbtPlayMusic.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        }

    }

    private class MusicDownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MydownloadID=musicDownloadManager.getDownloadID();
            long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (MydownloadID == ID && mBtnDownloadMusic != null) {
                mBtnDownloadMusic.setProgress(100);
            }
        }
    }

}
