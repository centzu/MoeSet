package com.example.helloworld.moeset.engine;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by helloworld on 2016/9/5.
 */
public class NetEaseMusicRequest {
    private Context context;
    private RequestQueue mRequestQueue;
    private String Keyword;

    public NetEaseMusicRequest(Context context, String keyword) {
        this.context = context;
        Keyword = keyword;
    }

    private RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public void keywordSearch( Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        if(Keyword==null){
            return;
        }
        String url = "http://s.music.163.com/search/get/?" + "type=1&s='" + URLEncoder.encode(Keyword) + "'&limit=20&offset=0";
        Log.i("sysout", "keywordSearch: "+url);
        request(Request.Method.GET, url, null, listener, errorListener);
    }

    private void request(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, jsonObject, listener, errorListener);
        getmRequestQueue().add(jsonObjectRequest);
    }

}
