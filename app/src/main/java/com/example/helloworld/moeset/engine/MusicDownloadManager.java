package com.example.helloworld.moeset.engine;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

/**
 * Created by helloworld on 2016/9/12.
 */
public class MusicDownloadManager{
    private Context context;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private String fileName;
    private String url;
    private String MusicDownloadPath;
    private Long downloadID;

    public MusicDownloadManager(Context context, String fileName, String url) {
        this.context = context;
        this.fileName = fileName;
        this.url = url;
        initData();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initData() {
        request=new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalFilesDir(context,"Music",fileName);
        request.setTitle(fileName);
        request.setDescription("下载完成后可点击打开");
        downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        downloadID= downloadManager.enqueue(request);

    }

    public Long getDownloadID() {
        return downloadID;
    }
}
