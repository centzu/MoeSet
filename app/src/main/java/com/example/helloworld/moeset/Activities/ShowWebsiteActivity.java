package com.example.helloworld.moeset.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.helloworld.moeset.R;

import java.util.Stack;

/**
 * Created by helloworld on 2016/8/29.
 */
public class ShowWebsiteActivity extends Activity {
    private WebView mNewsWedView;
    private String mUrl;
    private Stack<String> mUrlStack;
    private Button mBackButton;
    private ProgressBar mWebProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_website);
        initView();
    }

    private void initView() {
        //mWebProgress = (RelativeLayout) findViewById(R.id.rl_progressbar);
        mBackButton = (Button) findViewById(R.id.bt_news_website_back);
        mWebProgress = (ProgressBar) findViewById(R.id.pb_webprogress);
        mUrlStack = new Stack<String>();
        mUrl = getIntent().getStringExtra("url");
        mNewsWedView = (WebView) findViewById(R.id.wv_news);
        WebSettings webSettings = mNewsWedView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mNewsWedView.loadUrl(mUrl);
        mNewsWedView.setWebViewClient(new WebViewClient() {
            @Override
            /*加载新链接时调用的方法，包括点击webview中的链接*/
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!mUrlStack.empty()) {
                    if (!mUrlStack.peek().equals(url)) {
                        mUrlStack.push(url);
                    }
                } else {
                    mUrlStack.push(url);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("hexieshe")) {
                    view.loadUrl("javascript:" +
//                            "function hidebaidu(){" +
//                                    " var allElements=document.getElementsByTagName('a');" +
//                                    "for(var i=0;i<allElements.length;i++){" +
//                                     "if(allElements[i].className.indexOf('baidu')!=-1){" +
//                                    "allElements[i].style.display=\"none\";}}" +
//                                    " }hidebaidu();" +
                            "function HideAD(){" +
                            "var x=document.getElementsByTagName(\"footer\");" +
                            "for(var i=0;i<x.length;i++){x[i].style.display=\"none\";}" +
                            "var y=document.getElementsByTagName(\"aside\");y[0].style.display=\"none\";" +
                            "var z=document.getElementsByTagName(\"iframe\");z[0].style.display=\"none\";" +
                            "document.getElementById('w-1darpp-widget-isolated-host').style.display=\"none\";}HideAD();");
                }
                mNewsWedView.getSettings().setBlockNetworkImage(false);
                super.onPageFinished(view, url);
            }
        });
        mNewsWedView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    mWebProgress.setVisibility(View.GONE);
//                } else if (mWebProgress.getVisibility() == View.GONE) {
//                    mWebProgress.setVisibility(View.VISIBLE);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mWebProgress.setProgress(newProgress);
                if (newProgress == 100) {
                    mWebProgress.setVisibility(View.GONE);
                } else if (mWebProgress.getVisibility() == View.GONE) {
                    mWebProgress.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (String s : mUrlStack) {
//                    System.out.println(s);
//                }
                if (mUrlStack.size() == 1 || mUrlStack.empty() || mUrlStack.peek().equals(mUrl)) {
                    ShowWebsiteActivity.this.finish();
                } else {
                    mUrlStack.pop();
                    mNewsWedView.loadUrl(mUrlStack.peek());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
