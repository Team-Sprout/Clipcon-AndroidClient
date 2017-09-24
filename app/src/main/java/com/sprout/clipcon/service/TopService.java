package com.sprout.clipcon.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.activity.GroupActivity;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;
import com.sprout.clipcon.server.EndpointInBackGround;
import com.sprout.clipcon.transfer.RetrofitUploadData;

public class TopService extends Service {
    private View m_View;
    private WindowManager m_WindowManager;
    private WindowManager.LayoutParams m_Params;
    private static String textData;
    private static Uri uri;
    public static boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("delf", "[SYSTEM] TopService was created.");
        isRunning = true;
        // create <top_view> layout on Top
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_View = mInflater.inflate(R.layout.top_button, null);
        m_View.setOnTouchListener(onTouchListener);

        m_Params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        m_Params.gravity = Gravity.END | Gravity.BOTTOM;
        m_Params.horizontalMargin = 0.1f;
        m_Params.verticalMargin = 0.05f;

        m_WindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        m_WindowManager.addView(m_View, m_Params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_WindowManager.removeView(m_View);
        m_WindowManager = null;
        Log.d("delf", "[SYSTEM] TopService is destroyed.");
        isRunning = false;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };

    // event occurs when Top button pressed after clipboard changing
    public void onClickImageBtn(View v) {
        Log.d("delf", "[SYSTEM] floating button clicked.");
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Toast.makeText(getApplicationContext(), R.string.shareString, Toast.LENGTH_SHORT).show();

        if (!cm.hasPrimaryClip()) {
            Toast.makeText(this, "Clipboard is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) || cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            ClipData.Item item = cm.getPrimaryClip().getItemAt(0);
            textData = item.getText().toString();
            System.out.println(textData);

            showUploadProgressNoti();

            new EndpointInBackGround()
                    .setSendText(textData)
                    .execute(Message.UPLOAD, "text");

        } else if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) {

            ClipData.Item item = cm.getPrimaryClip().getItemAt(0);
            uri = item.getUri();
            System.out.println(uri);
        }
    }

    public void showUploadProgressNoti() {
        final int id = 1;
        final NotificationManager mNotifyManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

        mBuilder.setContentTitle("Data Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.icon_logo)
                .setProgress(0, 0, true);

        mNotifyManager.notify(id, mBuilder.build());

        RetrofitUploadData.UploadCallback uploadCallback = new RetrofitUploadData.UploadCallback() {
            @Override
            public void onUpload(long onGoing, long fileLength, double progressValue) { }
            @Override
            public void onComplete() {
                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.putExtra("History", "test");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                mBuilder.setProgress(100, 100, false);
                mBuilder.setContentText("Upload complete");
                mBuilder.setAutoCancel(true);
                mBuilder.setContentIntent(pendingIntent);

                mNotifyManager.notify(id, mBuilder.build());
            }
        };

        Endpoint.getUploader().setUploadCallback(uploadCallback);
    }
}
