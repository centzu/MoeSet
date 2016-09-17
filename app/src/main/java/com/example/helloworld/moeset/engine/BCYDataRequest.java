package com.example.helloworld.moeset.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by helloworld on 2016/9/15.
 */

public class BCYDataRequest {
    private String mUrl = "http://bcy.net/home/timeline/load";
    private Context context;
    private String mCookies;
    private RequestQueue mQueue;

    public BCYDataRequest(Context context) {
        this.context = context;
    }

    public RequestQueue getmQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
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

    public void getCookies() {
        getmQueue();
        //final String boundary = UUID.randomUUID().toString().replace("-", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://bcy.net/public/dologin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: success");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: error");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                //localHashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
                localHashMap.put("Referer", "http://bcy.net/public/login");
                localHashMap.put("Connection", "keep-alive");
                localHashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                localHashMap.put("Origin", "http://bcy.net");
                localHashMap.put("Upgrade-Insecure-Requests", "1");
                localHashMap.put("Accept-Language","zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
                return localHashMap;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("remember", "1");
                map.put("password", "lchlch5886900");
                map.put("email", "215160083@qq.com");
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeader = response.headers;
                mCookies = responseHeader.get("Set-Cookie");
                Iterator iter = responseHeader.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    Log.i(TAG, "parseNetworkResponse: " + key + ":" + responseHeader.get(key));
                }
                saveBcyCookies();
                try {
                    String data = new String(response.data, "UTF-8");
                    Log.i(TAG, "parseNetworkResponse: " + data);
                    return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return Response.error(new ParseError(e));
                }


            }
        };
        mQueue.add(stringRequest);
    }

    public void sendCookieAndLogin() {
        if (loadBcyCookies().equals(""))
            getCookies();
        getmQueue();
        StringRequest request = new StringRequest(Request.Method.POST, "http://bcy.net/home/user/index", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36");
                localHashMap.put("Cookie", loadBcyCookies());
                return localHashMap;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String dataString = new String(response.data, "UTF-8");
                    Log.i(TAG, "parseNetworkResponse: " + dataString);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

        };
        mQueue.add(request);
    }


}
