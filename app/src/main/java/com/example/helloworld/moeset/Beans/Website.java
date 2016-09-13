package com.example.helloworld.moeset.Beans;

import android.graphics.drawable.Drawable;

/**
 * Created by helloworld on 2016/8/28.
 */
public class Website {
    private Drawable icon;
    private String name;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Website(Drawable icon, String name,String url) {
        this.icon = icon;
        this.name = name;
        this.url=url;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
