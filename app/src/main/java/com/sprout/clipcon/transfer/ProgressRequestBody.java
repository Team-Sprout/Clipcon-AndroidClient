package com.sprout.clipcon.transfer;

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
        // int progressBarIndex = ProgressBarScene.getIndex();
        long fileLength = mFile.length();
        byte[] buffer = new byte[CHUNKSIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

//        while(progressBarIndex == -1) {
//            progressBarIndex = ProgressBarScene.getIndex();
//        }

        try {
            int read;

         /* update progress on UI thread */
            while ((read = in.read(buffer)) != -1) {
                // handler.post(new ProgressUpdater(uploaded, fileLength));

//                double progressValue = (100 * uploaded / fileLength);
//
//                if(fileLength < CHUNKSIZE) {
//                    ui.getProgressBarScene().setIndeterminateProgeress(progressBarIndex, false);
//                } else {
//                    ui.getProgressBarScene().setProgeress(progressBarIndex, progressValue, uploaded, fileLength, false);
//                }
//
//                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
//            if(fileLength < CHUNKSIZE) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            ui.getProgressBarScene().completeProgress(progressBarIndex);
//            ui.getMainScene().closeProgressBarStage(progressBarIndex);
        }
    }
}
