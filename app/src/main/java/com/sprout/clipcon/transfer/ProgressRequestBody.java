package com.sprout.clipcon.transfer;

import com.sprout.clipcon.server.Endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by heejeong on 2017. 9. 16..
 */

public class ProgressRequestBody extends RequestBody{
    private Endpoint endpoint = Endpoint.getInstance();
    private File mFile;
    private final int CHUNKSIZE = 0xFFFF; // 65536

    /** Setter */
    public void setmFile(File mFile) {
        this.mFile = mFile;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("multipart/form-data");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[CHUNKSIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;

         /* update progress on UI thread */
            while ((read = in.read(buffer)) != -1) {
                int progressValue = (int) (100 * uploaded / fileLength);

                // set progress value
                endpoint.getUploader().getUploadCallback().onUpload(uploaded, fileLength, progressValue);

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}
