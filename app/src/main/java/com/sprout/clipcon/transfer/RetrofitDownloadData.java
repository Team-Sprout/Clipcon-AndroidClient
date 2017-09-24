package com.sprout.clipcon.transfer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.History;
import com.sprout.clipcon.server.Endpoint;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heejeong on 2017. 9. 17..
 */

public class RetrofitDownloadData {

    private String userName = null;
    private String groupPK = null;
    private Contents requestContents; // Contents Info to download

    private final int CHUNKSIZE = 0xFFFF; // 65536
    private String charset = "UTF-8";

    // (check add field)
    private Context context;
    private DownloadCallback downloadCallback;
    private final String appDirectoryName = "Clipcon";

    public interface DownloadCallback {
        void onDownload(long fileSizeDownloaded, long fileSize, double progressValue);
        void onComplete();
    }

    /** Constructor
     * setting userName and groupPK. */
    public RetrofitDownloadData(String userName, String groupPK) {
        this.userName = userName;
        this.groupPK = groupPK;
    }

    /** Setter */
    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }
    /** Setter */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Send request the data you want to download
     *
     * @param downloadDataPK
     *            The primary key of the content to download
     */
    public void requestDataDownload(String downloadDataPK) throws MalformedURLException {
        // retrieving Contents from My History
        History myhistory = Endpoint.getUser().getGroup().getHistory();
        requestContents = myhistory.getContentsByPK(downloadDataPK);
         if (requestContents == null) {
            Log.d("delf", "[SYSTEM] requestContents is null");
        }

        // Parameter to be sent by the GET method
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userName", userName);
        parameters.put("groupPK", groupPK);
        parameters.put("downloadDataPK", downloadDataPK);

        // create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(RetrofitInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        // call object for the request
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.requestFileDataDownload(parameters);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    switch (requestContents.getContentsType()) {
                        case Contents.TYPE_STRING:
                            // Get String Object in Response Body
                            new Thread() {
                                public void run() {
                                    downloadStringData(response.body().byteStream());
                                }
                            }.start();
                            break;

                        case Contents.TYPE_IMAGE:
                            // Get Image Object in Response Body
                            new Thread() {
                                public void run() {
                                    downloadCapturedImageData(response.body().byteStream());
                                }
                            }.start();
                            break;

                        case Contents.TYPE_FILE:
                            // Save Real File(filename: fileOriginName) to Clipcon Folder Get Image Object in Response Body
                            new Thread() {
                                public void run() {
                                    String fileOriginName = requestContents.getContentsValue();
                                    downloadFileData(response.body().byteStream(), fileOriginName, response.body().contentLength());
                                }
                            }.start();
                            break;

                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable arg1) {
                System.out.println("Download onFailure");
            }

        });
    }

    /** Download String Data */
    private void downloadStringData(InputStream inputStream) {
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            stringBuilder = new StringBuilder();
            String line = null;

            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String deleteNewLineString = stringBuilder.toString();
        deleteNewLineString = deleteNewLineString.substring(0, deleteNewLineString.length() - 2); // \n\n 제거

        stringToClipboard(deleteNewLineString);

        downloadCallback.onComplete();
    }

    /**
     * Download Captured Image Data
     * Change to Image object from file form of Image data
     */
    private void downloadCapturedImageData(InputStream inputStream) {
       // inputStream -> bitmap -> file
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        Bitmap imageBitmapData = BitmapFactory.decodeStream(bufferedInputStream);

        imageToGallery(imageBitmapData);

        downloadCallback.onComplete();
    }

    /** Download File Data to Temporary folder */
    private void downloadFileData(InputStream inputStream, String fileName, long fileLength) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        File file = new File(path, fileName);

        Log.d("delf", "[SYSTEM] download path: " + path);

        try {
            OutputStream outStream = new FileOutputStream(file);
            // 읽어들일 버퍼크기를 메모리에 생성
            byte[] buf = new byte[CHUNKSIZE];
            int bytesRead = -1;

            long downloaded = 0;

            // 끝까지 읽어들이면서 File 객체에 내용들을 쓴다
            while ((bytesRead = inputStream.read(buf)) != -1) {
                outStream.write(buf, 0, bytesRead);
                downloaded += bytesRead;

                double progressValue = (100 * downloaded / fileLength);
                downloadCallback.onDownload(downloaded, fileLength, progressValue);

                if (bytesRead == -1) {
                    break;
                }
            }

            // Stream 객체를 모두 닫는다.
            outStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadCallback.onComplete();
    }

    /** Copy To String in Clipboard */
    private void stringToClipboard(String s) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", s);
        cm.setPrimaryClip(clip);
    }

    /** Save Bitmap Image To File in Gallery */
      private void imageToGallery(Bitmap testBitmap) {
        Log.d("delf", "[SYSTEM] image to gallery");
        OutputStream fOut = null;
        String fileName = "Image" + createName(System.currentTimeMillis()) + ".png";

        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appDirectoryName);

        imageRoot.mkdirs();
        final File file = new File(imageRoot, fileName);

        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        testBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "clipcon description");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put("_data", file.getAbsolutePath());

        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /** create image file name */
       private String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
}
