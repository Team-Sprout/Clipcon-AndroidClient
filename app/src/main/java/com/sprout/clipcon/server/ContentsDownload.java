package com.sprout.clipcon.server;

import android.graphics.Bitmap;
import android.util.Log;

import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.History;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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


public class ContentsDownload {

    // 다운로드 파일을 임시로 저장할 위치
    private final static String SERVER_URL = "http://delf.gonetis.com:8080/websocketServerModule";
    private final static String SERVER_SERVLET = "/DownloadServlet";

    private final String charset = "UTF-8";
    private HttpURLConnection httpConn;

    private String userName = null;
    private String groupPK = null;

    private Contents requestContents; // Contents Info to download
    // private String downloadDataPK; // Contents' Primary Key to download

    /**
     * Constructor
     * Setting userName and groupPK
     */
    public ContentsDownload(String userName, String groupPK) {
        this.userName = userName;
        this.groupPK = groupPK;
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
        if(requestContents == null) {
            Log.d("delf", "[SYSTEM] requestContents is null");
        }

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
                        // TODO: 17-05-13 save to history
                        Log.d("delf", "[CLIENT] received test data: " + stringData);
                        break;

                    case Contents.TYPE_IMAGE:
                        // TODO: 17-05-13 download and get image data
                        // TODO: 17-05-13 save image at local store
                        break;

                    case Contents.TYPE_FILE:
                        // downloadFileData();
                        break;

                    default:
                        System.out.println("어떤 형식에도 속하지 않음.");
                }
                System.out.println();

            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }
            httpConn.disconnect();

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
    private File downloadCapturedImageData(InputStream inputStream) {
        // TODO: 17-05-13 convert inputStream to file
        File file = new File("outFile.java");
        //  context.getFilesDir();

        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private File downloadFileData(InputStream inputStream, String fileName) throws FileNotFoundException {
        return null;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private String generateRequestParameter(String downloadDataPK) {
        return SERVER_URL + SERVER_SERVLET + "?" + "userName=" + userName + "&" + "groupPK=" + groupPK + "&" + "downloadDataPK=" + downloadDataPK;
    }
}
