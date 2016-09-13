package com.example.helloworld.moeset.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helloworld.moeset.Activities.ShowWebsiteActivity;
import com.example.helloworld.moeset.Beans.Website;
import com.example.helloworld.moeset.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.helloworld.moeset.R.id.iv_website_icon;

/**
 * Created by helloworld on 2016/8/28.
 */
public class NewsFragment extends Fragment {
    private View MainView;
    private ListView mlvNewsWebsites;
    private List<Website> mNewsWebsites;
    private String mCurrUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainView=inflater.inflate(R.layout.news_websites,container,false);
        mlvNewsWebsites= (ListView) MainView.findViewById(R.id.lv_news_websites);
        mNewsWebsites=new ArrayList<Website>();
        mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"萌口组","http://www.kou.moe/m/"));
        mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"和邪社","http://www.hexieshe.com/"));
        mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"卖个萌","http://mag.moe/"));
        mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"梦域动漫","http://www.moejam.com/m/"));
        mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"动漫之家轻小说","http://q.dmzj.com/"));
       // mNewsWebsites.add(new Website(getResources().getDrawable(R.drawable.koumoe),"萌否电台","http://moe.fm/"));
        mlvNewsWebsites.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mNewsWebsites.size();
            }

            @Override
            public Object getItem(int position) {
                return mNewsWebsites.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v=View.inflate(getActivity(),R.layout.news_website_item,null);
                TextView tvName= (TextView) v.findViewById(R.id.tv_website_name);
                ImageView ivIcon= (ImageView) v.findViewById(iv_website_icon);
                tvName.setText(mNewsWebsites.get(position).getName());
                ivIcon.setImageDrawable(mNewsWebsites.get(position).getIcon());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrUrl=mNewsWebsites.get(position).getUrl();
                        Intent intent=new Intent();
                        intent.putExtra("url",mCurrUrl);
                        intent.setClass(getActivity(), ShowWebsiteActivity.class);
                        startActivity(intent);
                    }
                });
                return v;
            }
        });
        return MainView;

    }
}
