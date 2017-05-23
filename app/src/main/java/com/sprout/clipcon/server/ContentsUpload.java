package com.sprout.clipcon.server;

import android.graphics.Bitmap;

public class ContentsUpload {
    public UploadData uploader;

    public ContentsUpload(String userName, String groupKey) {
        uploader = new UploadData(userName, groupKey);
    }

    public void upload(Bitmap bitmap) {
        uploader.uploadImageData(bitmap);
    }

    public void upload(String text) {
        uploader.uploadStringData(text);
    }

    public void uploadFile(String filePath) {
        uploader.uploadMultipartData(filePath);
    }
}