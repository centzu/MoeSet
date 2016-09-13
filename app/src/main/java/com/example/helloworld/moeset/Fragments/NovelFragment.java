package com.example.helloworld.moeset.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloworld.moeset.R;

/**
 * Created by helloworld on 2016/8/28.
 */
public class NovelFragment extends Fragment {
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  v=inflater.inflate(R.layout.news_website_item,container,false);
    }
}
