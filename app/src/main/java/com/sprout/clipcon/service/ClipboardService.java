package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class ClipboardService extends Service {

    private ClipboardManager mClipboardManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(changedListener);
        }
    }

    // add listener
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(changedListener);

        return super.onStartCommand(intent, flags, startId);
    }

    // listener for clipbaord changing
    private ClipboardManager.OnPrimaryClipChangedListener changedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            Log.d("delf", "[SYSTEM] clipboard changing detected");

            showTopButton();
        }
    };

    // handler to control Top button only to show 5 seconds
    private void showTopButton() {
        Log.d("delf", "[SYSTEM] show top button");
        Log.d("delf", "[SYSTEM] start topService");

        if(TopService.isRunning) {
            Log.d("delf", "[SYSTEM] TopService is running.");
        } else {
            Log.d("delf", "[SYSTEM] TopService is not running.");
        }
        startService(new Intent(getApplicationContext(), TopService.class));

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                stopService(new Intent(getApplicationContext(), TopService.class));
            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
        Log.d("delf", "[SYSTEM] showTopButton() is end");
    }
}
