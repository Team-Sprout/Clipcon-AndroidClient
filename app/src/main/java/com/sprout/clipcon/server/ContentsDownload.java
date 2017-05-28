package com.sprout.clipcon.server;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ContentsDownload {

    // 다운로드 파일을 임시로 저장할 위치
    private final static String SERVER_URL = "http://delf.gonetis.com:8080/websocketServerModule";
    private final static String SERVER_SERVLET = "/DownloadServlet";

    private final String charset = "UTF-8";
    private HttpURLConnection httpConn;

    private String userName = null;
    private String groupPK = null;

    private Contents requestContents; // Contents Info to download

    private Context context;
    private final String appDirectoryName = "Clipcon";
    // private String downloadDataPK; // Contents' Primary Key to download

    private DownloadCallback downloadCallback;

    public interface DownloadCallback {
        void onSuccess();
    }

    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    /**
     * Constructor
     * Setting userName and groupPK
     */
    public ContentsDownload(String userName, String groupPK) {
        this.userName = userName;
        this.groupPK = groupPK;
        // this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 다운로드하기 원하는 Data를 request 복수 선택은 File Data의 경우만 가능(추후 개선)
     *
     * @param downloadDataPK 다운로드할 Data의 고유키
     */
    public void requestDataDownload(String downloadDataPK) throws MalformedURLException {
        Log.d("delf", "[CLIENT] requestDataDownload(), pk is " + downloadDataPK);
        History myHistory = Endpoint.getUser().getGroup().getHistory();
        requestContents = myHistory.getContentsByPK(downloadDataPK);

        try {
            URL url = new URL(generateRequestParameter(downloadDataPK));
            httpConn = (HttpURLConnection) url.openConnection();
            Log.d("delf", "[CLIENT] complete connecting to server for sending download request");
            httpConn.setRequestMethod("GET");
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(false); // indicates GET method
            httpConn.setDoInput(true);

            // checks server's status code first
            int status = httpConn.getResponseCode();
            Log.d("delf", "[CLIENT] response received. status: " + status + ".");
            if (status == HttpURLConnection.HTTP_OK) {
                switch (requestContents.getContentsType()) {
                    case Contents.TYPE_STRING:
                        String stringData = downloadStringData(httpConn.getInputStream());
                        Log.d("delf", "[CLIENT] received test data: " + stringData);
                        break;

                    case Contents.TYPE_IMAGE:
                        Log.d("delf", "[CLIENT] received test data is image");
                        downloadImageData(httpConn.getInputStream());
                        break;

                    case Contents.TYPE_FILE:
                        Log.d("delf", "[CLIENT] received file");
                        String fileOriginName = requestContents.getContentsValue();
                         downloadFileData(httpConn.getInputStream(), fileOriginName);
                        Log.d("delf", "[CLIENT] complete downloading file.");
                        break;

                    default:
                        System.out.println("어떤 형식에도 속하지 않음.");
                }
                System.out.println();

            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }
            httpConn.disconnect();
            downloadCallback.onSuccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download String Data
     */
    private String downloadStringData(InputStream inputStream) {
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
        return stringBuilder.toString();
    }

    // test code
    private File downloadImageData(InputStream inputStream) throws IOException {

        // in -> file
        // in -> bit -> file
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
        imageToGallery(bmp);

        /*String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        Log.d("delf", "[SYSTEM] download path: " + path);
        File file = new File(path, fileName);
        OutputStream out = new FileOutputStream(file);

        int len = 0;
        while ((len = inputStream.read()) != -1) {
            out.write(len);
        }
        out.flush();
        out.close();
        Log.d("delf", "[SYSTEM] complete download file.");*/
        return null;
    }

    private File downloadFileData(InputStream inputStream, String fileName) throws IOException {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        File file = new File(path, fileName);
        Log.d("delf", "[SYSTEM] download path: " + path);
        try {
            OutputStream outStream = new FileOutputStream(file);
            // 읽어들일 버퍼크기를 메모리에 생성
            byte[] buf = new byte[1024]; // 충돌 시, 이전 숫자로 바꿀 것
            int len = 0;
            // 끝까지 읽어들이면서 File 객체에 내용들을 쓴다
            while ((len = inputStream.read(buf)) > 0) {
                outStream.write(buf, 0, len);
                outStream.flush();
            }
            // Stream 객체를 모두 닫는다.
            outStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private String generateRequestParameter(String downloadDataPK) {
        return SERVER_URL + SERVER_SERVLET + "?" + "userName=" + userName + "&" + "groupPK=" + groupPK + "&" + "downloadDataPK=" + downloadDataPK;
    }

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

    private String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
}
