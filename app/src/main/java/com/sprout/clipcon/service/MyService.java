package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class MyService extends Service {

    private ClipboardManager mClipboardManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    //// TODO: 2017. 4. 20. have to check whether it is called or not
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

        String action = intent.getAction();
        if(Intent.ACTION_SEND.equals(action)){
            System.out.println("adfsafadf");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    // listener for clipbaord changing
    private ClipboardManager.OnPrimaryClipChangedListener changedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {

            System.out.println("서비스 호출 테스트 ==== 11111 =======");

            Toast toast = Toast.makeText(getApplicationContext(), "Clipboard changed", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            showTopButton();

            System.out.println("서비스 호출 테스트 ==== 22222 =======");
        }
    };

    // handler to control Top button only to show 5 seconds
    private void showTopButton() {
        startService(new Intent(getApplicationContext(), TopService.class));

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                stopService(new Intent(getApplicationContext(), TopService.class));

            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
    }
}
