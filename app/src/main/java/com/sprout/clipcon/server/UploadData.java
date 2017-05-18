package com.sprout.clipcon.server;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UploadData {
    // public final static String SERVER_URL = "http://182.172.16.118:8080/websocketServerModule";
    // public final static String SERVER_URL = "http://223.194.157.244:8080/websocketServerModule";
    public final static String SERVER_URL = "http://delf.gonetis.com:8080/websocketServerModule";
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

    /**
     * Upload File Data
     *
     * @param fileFullPathList file path from clipboard
     */
    public void uploadMultipartData(ArrayList<String> fileFullPathList) {
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_URL + SERVER_SERVLET, charset);
            setCommonParameter(multipart);
            Log.d("delf", "attempt to uploading data");
            // create uploading file
            File firstUploadFile = new File(fileFullPathList.get(0));

			/* case: Single file data(not a folder), createFolder = FALSE */
            if (fileFullPathList.size() == 1 && firstUploadFile.isFile()) {
                System.out.println("\nSingle File Uploading~~\n");
                multipart.addFormField("createFolder", "FALSE");
                multipart.addFilePart("multipartFileData", firstUploadFile, "/");
            }
			/* case: Multiple file data, One or more folders, createFolder = TRUE */
            else {
                System.out.println("\nMultiple File Uploading~~\n");
                multipart.addFormField("createFolder", "TRUE");

                Iterator iterator = fileFullPathList.iterator();

                while (iterator.hasNext()) {
                    String fileFullPath = (String) iterator.next();

                    // create uploading file
                    File uploadFile = new File(fileFullPath);

                    System.out.println("<<fileFullPathList>>: " + fileFullPath);

					/* case: File */
                    if (uploadFile.isFile()) {
                        System.out.println("File Data Uploading~~");
                        multipart.addFilePart("multipartFileData", uploadFile, "/");
                    }
					/* case: Directory */
                    else if (uploadFile.isDirectory()) {
                        System.out.println("Directory Data Uploading~~");

                        // Initial value for relative path (Set starting index of root directory)
                        startIndex = uploadFile.getPath().lastIndexOf(uploadFile.getName());

                        multipart.addFormField("directoryData", uploadFile.getPath().substring(startIndex));
                        System.out.println("Directory name: " + uploadFile.getName() + ", Relative path: " + uploadFile.getPath().substring(startIndex));

                        subDirList(uploadFile, multipart);
                    }
                    System.out.println();
                }
            }

            List<String> response = multipart.finish();

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Setting <Relative path, File name> depending on the structure of the File Data
     * case directory: send relative path info using addFormField
     * case File: send real file data and relative path info using addFilePart
     */
    public void subDirList(File uploadFile, MultipartUtility multipart) {
        File[] fileList = uploadFile.listFiles(); // file data list in directory

        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
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
