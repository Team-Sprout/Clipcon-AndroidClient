package com.sprout.clipcon.server;

import android.graphics.Bitmap;

public class ContentsUpload {
    public UploadData uploader;

    private UploadCallback uploadCallback;

    public interface UploadCallback {
        void onSuccess();
    }

    public void setUploadCallback(UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
    }


    public ContentsUpload(String userName, String groupKey) {
        uploader = new UploadData(userName, groupKey);
    }

    public void uploadText(String text) {
        uploader.uploadStringData(text);
    }

    public void uploadImage(Bitmap bitmap) {
        uploader.uploadImageData(bitmap);
        uploadCallback.onSuccess();
    }

    public void uploadFile(String filePath) {
        uploader.uploadMultipartData(filePath);
        uploadCallback.onSuccess();
    }
}