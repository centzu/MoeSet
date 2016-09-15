package com.example.helloworld.moeset.engine;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by helloworld on 2016/9/15.
 */

public class BCYDataRequest {
    private String mUrl;
    private Context context;
    private String mCookies;

    private void login() {


    }

    private void saveBcyCookies() {
        SharedPreferences.Editor write = context.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
        write.putString("BcyCookies", mCookies);
        write.commit();
    }

    private String loadBcyCookies() {
        SharedPreferences read = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return read.getString("BcyCookies", "");
    }
    private void getCookies(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
