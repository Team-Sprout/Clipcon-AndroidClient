package com.sprout.clipcon.transfer;

/**
 * Created by heejeong on 2017. 9. 14..
 */

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUploadData {

    private UploadCallback uploadCallback;

    public interface UploadCallback {
        void onUpload(long fileSizeDownloaded, long fileSize, double progressValue);
        void onComplete();
    }

    private String userName = null;
    private String groupPK = null;

    // create Retrofit instance
    public Retrofit.Builder builder = new Retrofit.Builder().baseUrl(RetrofitInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create());
    public Retrofit retrofit = builder.build();

    // get client & call object for the request
    public RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    public Call<ResponseBody> call = null;

    /** Constructor
     * setting userName and groupPK. */
    public RetrofitUploadData(String userName, String groupPK) {
        this.userName = userName;
        this.groupPK = groupPK;
    }

    /** Setter */
    public void setUploadCallback(UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
    }

    public UploadCallback getUploadCallback() {
        return this.uploadCallback;
    }

    /** Upload String Data */
    public void uploadStringData(String stringData) {

        // add another part within the multipart request
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody grouppk = RequestBody.create(MediaType.parse("text/plain"), groupPK);
        RequestBody stringdata = RequestBody.create(MediaType.parse("text/plain"), stringData);

        call = retrofitInterface.requestStringDataUpload(username, grouppk, stringdata);
        callResult(call);
    }

    /** Upload Captured Image Data in Clipboard */
    public void uploadImageData(Bitmap bitmapImage) {

        // add another part within the multipart request
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody grouppk = RequestBody.create(MediaType.parse("text/plain"), groupPK);

        // convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        // RequestBody imagedata = RequestBody.create(MediaType.parse("image/png"), imageInByte);
        RequestBody imagedata = RequestBody.create(MediaType.parse("application/octet-stream"), bitmapData);

        call = retrofitInterface.requestImageDataUpload(username, grouppk, imagedata);
        callResult(call);
    }

    /** Upload File Data */
    public void uploadMultipartData(String fileFullPath) {

        // create uploading file
        File firstUploadFile = new File(fileFullPath);

        // add another part within the multipart request
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody grouppk = RequestBody.create(MediaType.parse("text/plain"), groupPK);

        // create RequestBody instance (from file)
        ProgressRequestBody progressFilePart = new ProgressRequestBody();
        progressFilePart.setmFile(firstUploadFile);

        /* Single file data(not a folder) */
        MultipartBody.Part file = MultipartBody.Part.createFormData("fileData", firstUploadFile.getName(), progressFilePart);

        // finally, execute the request
        call = retrofitInterface.requestFileDataUpload(username, grouppk, file);
        callResult(call);
    }

    /** logging method- check for a successful response */
    public void callResult(Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Upload success");
                uploadCallback.onComplete();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable arg1) {
                System.out.println("Upload onFailure");
            }
        });
    }
}
