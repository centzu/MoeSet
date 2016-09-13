package com.example.helloworld.moeset.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.helloworld.moeset.Activities.PlayMusicActivity;
import com.example.helloworld.moeset.Beans.NetEaseResponseBeans.MusicResponse;
import com.example.helloworld.moeset.Beans.NetEaseResponseBeans.Song;
import com.example.helloworld.moeset.Beans.SongInfo;
import com.example.helloworld.moeset.R;
import com.example.helloworld.moeset.Service.MusicPlayerService;
import com.example.helloworld.moeset.engine.NetEaseMusicRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helloworld on 2016/8/28.
 */
public class MusicFragment extends Fragment {
    private View MainView;
    private EditText metKeyWord;
    private Button mbtOk;
    private NetEaseMusicRequest netEaseMusicRequest;
    private String mKeyWord;
    private List<SongInfo> mSongsInfo;
    private int songsCount;
    private ListView mlvMusics;
    private ImageView mivMusicStop;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainView = inflater.inflate(R.layout.music_fragment, container, false);
        metKeyWord = (EditText) MainView.findViewById(R.id.et_search);
        mbtOk = (Button) MainView.findViewById(R.id.bt_search_start);
        mlvMusics = (ListView) MainView.findViewById(R.id.lv_search_music);
        mivMusicStop= (ImageView) MainView.findViewById(R.id.iv_music_stop);
        mSongsInfo = new ArrayList<SongInfo>();
        metKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    mbtOk.performClick();
                }
                return false;
            }
        });
        mbtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSongsInfo.isEmpty())
                    mSongsInfo.clear();
                mKeyWord = metKeyWord.getText().toString();
                if (mKeyWord.trim().equals(""))
                    return;
                netEaseMusicRequest = new NetEaseMusicRequest(getActivity(), mKeyWord);
                netEaseMusicRequest.keywordSearch(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!response.toString().contains("result")) {
                            mSongsInfo.add(new SongInfo("找不到了404", "", "", ""));
                            mlvMusics.setAdapter(new SongListAdapter());
                            return;
                        }
                        Gson gson = new Gson();
                        MusicResponse musicResponse = gson.fromJson(response.toString(), new TypeToken<MusicResponse>() {
                        }.getType());
                        songsCount=musicResponse.getResult().getSongs().size();
                        for (int i = 0; i < songsCount; i++) {
                            SongInfo songInfo = new SongInfo();
                            Song song = musicResponse.getResult().getSong(i);
                            //System.out.println(song.getName());
                            if (song.getName() != null)
                                songInfo.setName(song.getName());
                            //System.out.println(song.getAudio());
                            if (song.getAudio() != null)
                                songInfo.setUrl(song.getAudio());
                            // System.out.println(song.getArtist(0).getName());
                            if (song.getArtist(0).getName() != null) {
                                songInfo.setArtist(song.getArtist(0).getName());
                            } else {
                                songInfo.setArtist("unknown");
                            }
                            //System.out.println(song.getAlbum().getPicUrl());
                            if (song.getAlbum().getPicUrl() != null) {
                                songInfo.setPicUrl(song.getAlbum().getPicUrl());
                            } else {
                                songInfo.setPicUrl(" ");
                            }
                            if(song.getAlbum().getName()!=null){
                                songInfo.setAlbumName("-"+song.getAlbum().getName());
                            }else {
                                songInfo.setAlbumName(" ");
                            }

                            mSongsInfo.add(songInfo);
                        }
                        mlvMusics.setAdapter(new SongListAdapter());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(222);
                    }
                });
            }
        });
        mivMusicStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getActivity().stopService(new Intent(getActivity(), MusicPlayerService.class));
            }
        });
        return MainView;
    }

    private class SongListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSongsInfo.size();
        }

        @Override
        public SongInfo getItem(int position) {
            return mSongsInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if (mSongsInfo.get(0).getName().equals("找不到了404")) {
                view = View.inflate(getActivity(), R.layout.music_search_result_item, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_music_name);
                textView.setText("找不到了");
                return view;
            }
            ViewHolder holder = null;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.music_search_result_item, null);
                holder = new ViewHolder();
                holder.tvArtist = (TextView) view.findViewById(R.id.tv_music_artist);
                holder.tvName = (TextView) view.findViewById(R.id.tv_music_name);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.tvName.setText(mSongsInfo.get(position).getName());
            holder.tvArtist.setText(mSongsInfo.get(position).getArtist()+mSongsInfo.get(position).getAlbumName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println(mSongsInfo.get(position).getPicUrl());
                    Intent intent=new Intent(getActivity(), PlayMusicActivity.class);
                    intent.putExtra("MusicName",mSongsInfo.get(position).getName());
                    intent.putExtra("MusicArtist",mSongsInfo.get(position).getArtist());
                    intent.putExtra("MusicUrl",mSongsInfo.get(position).getUrl());
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    static class ViewHolder {
        private TextView tvName;
        private TextView tvArtist;

    }

}
