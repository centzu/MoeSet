package com.example.helloworld.moeset.engine;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

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
        StringRequest stringRequest= new StringRequest(Request.Method.POST, "http://bcy.net/public/dologin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String,String> responseHeader=response.headers;
                mCookies=responseHeader.get("Set-Cookie");
                saveBcyCookies();
                return super.parseNetworkResponse(response);

            }
        };
    }

}
