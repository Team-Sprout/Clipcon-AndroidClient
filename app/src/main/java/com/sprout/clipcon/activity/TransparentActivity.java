package com.sprout.clipcon.activity;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;
import com.sprout.clipcon.server.EndpointInBackGround;
import com.sprout.clipcon.transfer.RetrofitUploadData;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Yongwon on 2017. 5. 1..
 */

public class TransparentActivity extends Activity {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Uri uri;
    private static Bitmap bitmap;

    //ask user about permission to save image into basic gallery apps.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);

        String action = getIntent().getAction();
        String type = getIntent().getType();

        if (Intent.ACTION_SEND.equals(action)) {
            getPermission();
            uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

            Log.d("delf", "[DEBUG] shared date type is " + type);
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String mimeType = mime.getExtensionFromMimeType(cR.getType(uri)); // get "file name extension"
            Log.d("delf", "[DEBUG] shared date mime type is " + mimeType);

            showUploadProgressNoti();

            if (type.startsWith("image/")) {
                Toast.makeText(getApplicationContext(), R.string.shareImage, Toast.LENGTH_SHORT).show();

                bitmap = getBitmapByUri(uri);
                new EndpointInBackGround()
                        .setSendBitmapImage(bitmap)
                        .execute(Message.UPLOAD, "image");
            } else {
                Toast.makeText(getApplicationContext(), R.string.shareFile, Toast.LENGTH_SHORT).show();
                Log.d("delf", "[DEBUG] uri.getPath() = " + uri.getPath());

                String filePath = getPathFromUri(uri);
                Log.d("hee", "[DEBUG] filePath = " + filePath);

                new EndpointInBackGround()
                        .setFilePath(filePath)
                        .execute(Message.UPLOAD, "file");
            }

        }

        finish();
    }

    private String getPathFromUri(Uri uri){
        String path;
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null );
            cursor.moveToNext();
            path = cursor.getString( cursor.getColumnIndex( "_data" ) );
            Log.d("hee", "[DEBUG] path: " + path);
            cursor.close();
        } catch (NullPointerException e) {
            path = uri.getPath();
        }
        return path;
    }

    private Bitmap getBitmapByUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
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
            public void onUpload(long onGoing, long fileLength, double progressValue) {
                double onGoingMB = ((onGoing / 1024.0) / 1024.0);
                double fileLengthMB = ((fileLength / 1024.0) / 1024.0);

                DecimalFormat dec = new DecimalFormat("0.0");

                String progress = (int) progressValue + "% (" + dec.format(onGoingMB) + " / " + dec.format(fileLengthMB) + " MB)";

                mBuilder.setProgress(100, (int)progressValue, false);
                mBuilder.setContentText("Uploading...  " + progress);
                mBuilder.setAutoCancel(true);

                mNotifyManager.notify(id, mBuilder.build());
            }
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

