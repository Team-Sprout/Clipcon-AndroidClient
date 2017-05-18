package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.EndpointInBackGround;

public class TopService extends Service {
    private View m_View;
    private WindowManager m_WindowManager;
    private WindowManager.LayoutParams m_Params;
    private static String textData;
    private static Uri uri;
    public static boolean isRunning = false;
    private boolean tmpFlag = true;

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
        m_Params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
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
        /*MyClipboard clipboardManager = MyClipboard.getInstance();

        if(clipboardManager.isEmpty()) {
            Log.d("delf", "[SYSTEM] clipboard is empty.");
            Toast.makeText(this, "empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (clipboardManager.isStringType()) {
            Log.d("delf", "[SYSTEM] clipboard data is text type.");
            textData = clipboardManager.getTextInClipboard();
            new EndpointInBackGround().execute(Message.UPLOAD, "text");
            Toast.makeText(this, "Text ( plain + html ) " + textData, Toast.LENGTH_SHORT).show();

        } else if(clipboardManager.isImageType()) {
            Log.d("delf", "[SYSTEM] clipboard data is image type.");
            Bitmap image = clipboardManager.getImageInClipboard();
            Toast.makeText(this, "Uri " + uri, Toast.LENGTH_SHORT).show();

        } else {
            Log.d("delf", "[SYSTEM] clipboard data type is unknown.");
            // exception
        }*/


        if (!cm.hasPrimaryClip()) {
            Toast.makeText(this, "empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) || cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            System.out.println("Text");
            String temp = cm.getPrimaryClipDescription().getMimeType(0);

            ClipData.Item item = cm.getPrimaryClip().getItemAt(0);
            textData = item.getText().toString();
            System.out.println(textData);

            new EndpointInBackGround()
                    .setSendText(textData)
                    .execute(Message.UPLOAD, "text");

            Toast.makeText(this, "Text ( plain + html ) " + temp, Toast.LENGTH_SHORT).show();

        } else if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) {
            System.out.println("Image");

            ClipData.Item item = cm.getPrimaryClip().getItemAt(0);
            uri = item.getUri();
            System.out.println(uri);

            Toast.makeText(this, "Uri " + uri, Toast.LENGTH_SHORT).show();
        }
    }
}
