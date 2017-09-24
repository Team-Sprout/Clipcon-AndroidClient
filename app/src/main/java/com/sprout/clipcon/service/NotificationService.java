package com.sprout.clipcon.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sprout.clipcon.R;
import com.sprout.clipcon.activity.GroupActivity;
import com.sprout.clipcon.server.Endpoint;

public class NotificationService extends Service {
    Endpoint endpoint = Endpoint.getInstance();

    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private PendingIntent pendingIntent;
    public Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("delf", "[SYSTEM] NotificationService is binded.");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("delf", "[SYSTEM] NotificationService: onStartCommand()");
        myServiceHandler handler = new myServiceHandler();
        endpoint.setHandler(handler);
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        Log.d("delf", "[SYSTEM] NotificationService: onDestroyed()");
    }

    class myServiceHandler extends Handler {

        final int id = 1;
        @Override
        public void handleMessage(android.os.Message msg) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            intent = new Intent(getApplicationContext(), GroupActivity.class);
            intent.putExtra("History", "test");
            intent.setAction("NOW");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder = new Notification.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.icon_logo);
            builder.setTicker("new Clipcon"); //** 이 부분은 확인 필요
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("Clipcon Alert"); //** 큰 텍스트로 표시
            builder.setContentText("History data is updated"); //** 작은 텍스트로 표시
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX); //** MAX 나 HIGH로 줘야 가능함
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_ONLY_ALERT_ONCE);
            builder.setContentIntent(pendingIntent);

            notificationManager.notify(id, builder.build());
        }
    };
}

