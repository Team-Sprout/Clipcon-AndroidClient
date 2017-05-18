package com.sprout.clipcon.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.activity.GroupActivity;
import com.sprout.clipcon.server.Endpoint;

public class NotificationService extends Service {
    NotificationManager notifi_M;
    // ServiceThread thread;
    Endpoint endpoint = Endpoint.getInstance();
    Notification notifi;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("delf", "[SYSTEM] NotificationService is binded.");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("delf", "[SYSTEM] NotificationService: onStartCommand()");
        notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        endpoint.setHandeler(handler);
        // thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        Log.d("delf", "[SYSTEM] NotificationService: onDestroyed()");
        // thread.stopForever();
        // thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            notifi = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Content Title")
                    .setContentText("Content Text")
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("알림!!!")
                    .setContentIntent(pendingIntent)
                    .build();

            //소리추가
            notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //확인하면 자동으로 알림이 제거 되도록
            notifi.flags = Notification.FLAG_AUTO_CANCEL;


            notifi_M.notify( 777 , notifi);

            //토스트 띄우기
            Toast.makeText(getApplicationContext(), "뜸?", Toast.LENGTH_LONG).show();
        }
    };
}

