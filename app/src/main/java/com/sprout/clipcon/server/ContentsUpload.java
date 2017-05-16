package com.sprout.clipcon.server;

import android.graphics.Bitmap;
import android.util.Log;

public class ContentsUpload {
    public UploadData uploader;

    public ContentsUpload(String userName, String groupKey) {
        uploader = new UploadData(userName, groupKey);
    }

    public void upload(Bitmap bitmap) {

        uploader.uploadImageData(bitmap);
    }
    public void upload(String text) {
        Log.d("delf", "[SYSTEM] in upload() upload the string: " + text);
        uploader.uploadStringData(text);
    }
}