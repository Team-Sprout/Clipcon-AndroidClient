package com.sprout.clipcon.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.sprout.clipcon.R;

/**
 * Created by Yongwon on 2017. 5. 1..
 */

public class TransparentActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);

        System.out.println("투명액티비티 시작");
        String action = getIntent().getAction();

        if(Intent.ACTION_SEND.equals(action)){

            System.out.println("********* ACTION_SEND called *********");
            Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            System.out.println("Uri는 "+ uri);

//            try {
//                ImageView testImage = (ImageView) getView().findViewById(R.id.testImageView);
//                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                testImage.setImageBitmap(bm);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            // insert image uri to clipboard
            ClipData clip = ClipData.newRawUri("test", uri);
            ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(clip);
        }



        finish();
        System.out.println("투명액티비티 종료");
    }
}
