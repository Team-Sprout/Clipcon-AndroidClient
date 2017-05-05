package com.sprout.clipcon.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sprout.clipcon.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yongwon on 2017. 5. 1..
 */

public class TransparentActivity extends Activity{

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Uri uri;

    //ask user about permission to save image into basic gallery apps.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);

        System.out.println("투명액티비티 시작");
        String action = getIntent().getAction();

        if(Intent.ACTION_SEND.equals(action)){

            System.out.println("********* ACTION_SEND called *********");

            String subject1 = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String subject2 = getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT);
            System.out.println("urlFromWeb1 = " +subject1);
            System.out.println("urlFromWeb2 = " +subject2);



            uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            System.out.println("Uri는 "+ uri);


//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//                } else {
//
//                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//
//                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                    // app-defined int constant. The callback method gets the
//                    // result of the request.
//                }
//            }else {
//                bitmapToImage();
//
//            }

            // insert image uri to clipboard
            ClipData clip = ClipData.newRawUri("test", uri);
            ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(clip);
        }

        finish();
        System.out.println("투명액티비티 종료");
    }

//    public void bitmapToImage() {
//        //// TODO: 2017. 5. 3. have to check where the Image saved (Internal Path)
//        // save shared Image
//        try {
//            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//
////            MediaStore.Images.Media.insertImage(getContentResolver(), bm, "test.png", "testCheck");
//
//            File file = new File("test.png");
//
//            FileOutputStream fos = openFileOutput("test.png", 0);
//
//            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
//
//            System.out.println("PNG TEST ======== " + bm.compress(Bitmap.CompressFormat.PNG, 100, fos));
//            fos.flush();
//            fos.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
