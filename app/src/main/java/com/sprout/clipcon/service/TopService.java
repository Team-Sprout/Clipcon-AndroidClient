package com.sprout.clipcon.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sprout.clipcon.R;


public class TopService extends Service {
    private View m_View;
    private WindowManager m_WindowManager;
    private WindowManager.LayoutParams  m_Params;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println(" 에러 테스트 1 ");

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
        m_Params.gravity = Gravity.LEFT | Gravity.TOP;
        m_Params.horizontalMargin = 0.1f;
        m_Params.verticalMargin = 0.05f;

        System.out.println(" 에러 테스트 2 ");

        m_WindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        m_WindowManager.addView(m_View, m_Params);

        System.out.println(" 에러 테스트 3 ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // delete layout
        m_WindowManager.removeView(m_View);
        m_WindowManager = null;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_UP:
                    break;
            }

            return false;
        }
    };

    // event occurs when Top button pressed after clipboard changing
    public void onImageBtnTest(View v) {

        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        if(!cm.hasPrimaryClip()) {
            Toast.makeText(this, "empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) || cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            String temp = cm.getPrimaryClipDescription().getMimeType(0);

            Toast.makeText(this, "Text ( plain + html ) " + temp, Toast.LENGTH_SHORT).show();
        }

//        ClipData clip = cm.getPrimaryClip();
//        ClipData.Item item = clip.getItemAt(0);
//        TextView pasteText = (TextView) findViewById(R.id.pasteText);
//        pasteText.setText(item.getText());

    }
}
