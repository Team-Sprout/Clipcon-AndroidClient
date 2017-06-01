package com.sprout.clipcon.server;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadData {
//    public final static String SERVER_URL = "http://delf.gonetis.com:8080/websocketServerModule";
    public final static String SERVER_URL = "http://223.194.159.121:8080/websocketServerModule";

    public final static String SERVER_SERVLET = "/UploadServlet";
    private String charset = "UTF-8";

    private String userName = null;
    private String groupPK = null;
    private int startIndex = 0;

    /**
     * Constructor
     * setting userName and groupPK.
     */
    public UploadData(String userName, String groupPK) {
        this.userName = userName;
        this.groupPK = groupPK;
    }

    /**
     * Upload String Data
     */
    public void uploadStringData(String stringData) {
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_URL + SERVER_SERVLET, charset);
            setCommonParameter(multipart);

            multipart.addFormField("createFolder", "FALSE");
            multipart.addFormField("stringData", stringData);
            System.out.println("stringData: " + stringData);

            List<String> response = multipart.finish();

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void uploadImageData(Bitmap bitmapImage) {
        try {
            Log.d("delf", "[SYSTEM] start uploadImageData()");
            MultipartUtility multipart = new MultipartUtility(SERVER_URL + SERVER_SERVLET, charset);
            setCommonParameter(multipart);

            multipart.addFormField("createFolder", "FALSE");
            multipart.addImagePart("imageData", bitmapImage);

            Log.d("delf", "[SYSTEM] start generate response");
            List<String> response = multipart.finish();

            for (String line : response) {
                Log.d("delf", "[SYSTEM] response line: " + line);
            }

        } catch (IOException ex) {
            Log.e("exception", ex.toString());
        }
    }

    public void uploadMultipartData(String fileFullPath) {
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_URL + SERVER_SERVLET, charset);
            setCommonParameter(multipart);
            Log.d("delf", "attempt to uploading data");
            // create uploading file
            File firstUploadFile = new File(fileFullPath);

            multipart.addFilePart("fileData", firstUploadFile, "/");

            List<String> response = multipart.finish();

            Log.d("delf", "[SYSTEM] print the response -- ");
            for (String line : response) {
                Log.d("delf", "      " + line);
            }
            Log.d("delf", "[SYSTEM] -- response end");
        } catch (IOException ex) {
            Log.e("delf", ex.getStackTrace().toString());
        }
    }

    /*public String getRealImagePath(Uri uriPath) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uriPath, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        String path = cursor.getString(index);
        path = path.substring(5);
        return path;
    } */

    /**
     * Setting <Relative path, File name> depending on the structure of the File Data
     * case directory: send relative path info using addFormField
     * case File: send real file data and relative path info using addFilePart
     */
    public void subDirList(File uploadFile, MultipartUtility multipart) {
        File[] fileList = uploadFile.listFiles(); // file data list in directory

        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            /* case: There is another file inside the file to upload */
            if (file.isFile()) {
                multipart.addFilePart("multipartFileData", file, getFileRelativePath(file));
                System.out.println("File name: " + file.getName() + ", Relative path: " + getFileRelativePath(file));
            }
            /* case: There is a subdirectory inside the file to upload, Rediscover */
            else if (file.isDirectory()) {
                multipart.addFormField("directoryData", file.getPath().substring(startIndex));
                // subDirList(file.getCanonicalPath().toString());
                subDirList(file, multipart);
                System.out.println("Directory name: " + file.getName() + ", Relative path: " + file.getPath().substring(startIndex));
            }
        }
    }

    /**
     * Get relative path info
     */
    public String getFileRelativePath(File file) {
        String filePath = file.getPath();
        String fileName = file.getName();
        int endIndex = filePath.lastIndexOf(fileName);

        // relative path info except file name
        return filePath.substring(startIndex, endIndex - 1);
    }

    /**
     * Parameter to be set in common for all data
     * userName, groupPK, uploadTime
     */
    public void setCommonParameter(MultipartUtility multipart) {
        multipart.addHeaderField("User-Agent", "Heeee");
        multipart.addFormField("userName", userName);
        multipart.addFormField("groupPK", groupPK);
        multipart.addFormField("uploadTime", uploadTime());
    }

    /**
     * @return Current Time YYYY-MM-DD HH:MM:SS
     */
    public String uploadTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, a hh:mm:ss");
        return sdf.format(date).toString();
    }
}
