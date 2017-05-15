package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sprout.clipcon.exception.ClipboardEmptyException;

/**
 * Created by delf on 17-05-12.
 */

public class MyClipboard extends Service {

    private ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    private static MyClipboard uniqueMyClipboard;

    private MyClipboard() {

    }

    public static MyClipboard getInstance() {
        if (uniqueMyClipboard == null) {
            uniqueMyClipboard = new MyClipboard();
        }
        return uniqueMyClipboard;
    }

    public String getTextInClipboard() { // probably change static
        if(!cm.hasPrimaryClip()) {
            throw new ClipboardEmptyException(); // test code of delf
        } else if (!cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) && !cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            return null;
        }
        return cm.getPrimaryClip().getItemAt(0).getText().toString();
    }

    public Bitmap getImageInClipboard() {
        // TODO: 17-05-13 iamge
        return null;
    }

    public void insertDataInClipboard(String text) {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
