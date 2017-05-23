package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by delf on 17-05-12.
 */

public class MyClipboard extends Service {

    private ClipboardManager cm;
    private static MyClipboard uniqueMyClipboard;

    private MyClipboard() {
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Log.d("delf", "[SYSTEM] clipboard manager is created.");
    }

    private void getClopboard() {
        // cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static MyClipboard getInstance() {
        if (uniqueMyClipboard == null) {
            uniqueMyClipboard = new MyClipboard();
        }
        return uniqueMyClipboard;
    }

    public String getTextInClipboard() { // probably change static
        return cm.getPrimaryClip().getItemAt(0).getText().toString();
    }


    public boolean isEmpty() {
        return !cm.hasPrimaryClip();
    }

    public boolean isStringType() {
        return (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) || cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML));
    }

    public boolean isImageType() {
        return cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST);
    }

    public void insertDataInClipboard(String text) {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}