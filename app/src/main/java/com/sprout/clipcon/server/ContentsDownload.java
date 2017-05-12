package com.sprout.clipcon.server;

import android.media.Image;
import android.util.Log;

import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.History;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ContentsDownload {

    // 다운로드 파일을 임시로 저장할 위치
    private final String DOWNLOAD_LOCATION = "C:\\User\\delf\\desktop"; // 충돌 유도

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

        // 내가 속한 Group의 History를 가져온다. 수정 필요.
        History myhistory = Endpoint.getUser().getGroup().getHistory();

    /*    // Retrieving Contents from My History
        requestContents = myhistory.getContentsByPK(downloadDataPK);
        // Type of data to download
        String contentsType = requestContents.getContentsType();

        // Parameter to be sent by the GET method
        String parameters = "userName=" + userName + "&" + "groupPK=" + groupPK + "&" + "downloadDataPK=" + downloadDataPK;*/

        try {
            // URL url = new URL(SERVER_URL + SERVER_SERVLET + "?" + parameters);
            URL url = new URL(SERVER_URL + SERVER_SERVLET + "?" + "1");
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
                /*switch (contentsType) {
                    case Contents.TYPE_STRING:
                        break;

                    case Contents.TYPE_IMAGE:
                        break;

                    case Contents.TYPE_FILE:
                        // downloadFileData();
                        break;

                    default:
                        System.out.println("어떤 형식에도 속하지 않음.");
                }*/
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

    private Image downloadCapturedImageData(InputStream inputStream) {
        return null;
    }

    private File downloadFileData(InputStream inputStream, String fileName) throws FileNotFoundException {
        return null;
    }
}
