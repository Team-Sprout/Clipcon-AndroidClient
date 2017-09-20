package com.sprout.clipcon.transfer;

import android.graphics.Bitmap;

public class ContentsUploadAdapter {
    public RetrofitUploadData uploader;

    private UploadCallback uploadCallback;

    public interface UploadCallback {
        void onSuccess();
    }

    /** Setter */
    public void setUploadCallback(UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
    }

    public ContentsUploadAdapter(String userName, String groupKey) {
        uploader = new RetrofitUploadData(userName, groupKey);
    }

    public void uploadText(String text) {
        uploader.uploadStringData(text);
    }
    public void uploadImage(Bitmap bitmap) {
        uploader.uploadImageData(bitmap);
//        uploadCallback.onSuccess();
    }
    public void uploadFile(String filePath) {
        uploader.uploadMultipartData(filePath);
//        uploadCallback.onSuccess();
    }
}