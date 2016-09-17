package com.example.helloworld.moeset;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.helloworld.moeset.Fragments.MusicFragment;
import com.example.helloworld.moeset.Fragments.NewsFragment;
import com.example.helloworld.moeset.Fragments.NovelFragment;
import com.example.helloworld.moeset.engine.BCYDataRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private List<RadioButton> mRadioButtons;
    private RadioGroup mRadioGroup;
    private HomePagerAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        BCYDataRequest r=new BCYDataRequest(this);
        r.getCookies();
       // r.sendCookieAndLogin();
        NewsFragment newsFragment = new NewsFragment();
        MusicFragment musicFragment = new MusicFragment();
        NovelFragment novelFragment = new NovelFragment();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(newsFragment);
        mFragments.add(musicFragment);
        mFragments.add(novelFragment);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_select);
        RadioButton rbNews = (RadioButton) findViewById(R.id.rb_news);
        RadioButton rbMusic = (RadioButton) findViewById(R.id.rb_music);
        RadioButton rbNovel = (RadioButton) findViewById(R.id.rb_novel);
        mRadioButtons = new ArrayList<RadioButton>();
        mRadioButtons.add(rbNews);
        mRadioButtons.add(rbMusic);
        mRadioButtons.add(rbNovel);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        setPager(0);
                        break;
                    case R.id.rb_music:
                        setPager(1);
                        break;
                    case R.id.rb_novel:
                        setPager(2);
                        break;
                    default:
                        break;
                }

            }
        });

        mViewPager = (ViewPager) findViewById(R.id.vp_home);
        mViewPager.addOnPageChangeListener(this);

        mAdapter = new HomePagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        setPager(0);


    }

    private void setPager(int tag) {
        int length = mFragments.size();
        mViewPager.setCurrentItem(tag, false);
        Animation alphaAnimate = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimate.setDuration(300);
        for (int i = 0; i < length; i++) {
            if (tag == i) {
                mRadioButtons.get(i).startAnimation(alphaAnimate);
                mRadioButtons.get(i).setBackgroundColor(Color.parseColor("#ffffff"));

            } else {
                mRadioButtons.get(i).setBackgroundColor(Color.parseColor("#cc66ff"));

            }

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.rb_news);
                break;
            case 1:
                mRadioGroup.check(R.id.rb_music);
                break;
            case 2:
                mRadioGroup.check(R.id.rb_novel);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class HomePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;


        public HomePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
