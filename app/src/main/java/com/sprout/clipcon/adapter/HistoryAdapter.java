package com.sprout.clipcon.adapter;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.ContentsDownload;
import com.sprout.clipcon.server.Endpoint;
import com.sprout.clipcon.server.EndpointInBackGround;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private ArrayList<Contents> contentsList;

    private Bitmap tmpBitmap;

    public HistoryAdapter(Context context, ArrayList<Contents> contentsList) {
        this.context = context;
        this.contentsList = contentsList;
        Endpoint.getDownloader().setContext(context);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contents, null);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);

        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        final Contents contents = contentsList.get(position);
        holder.sender.setText(contents.getUploadUserName());
        holder.time.setText(contents.getUploadTime());

        switch (contents.getContentsType()) {
            case Contents.TYPE_IMAGE:
//                Bitmap tmpBitmap = getBitmapByBase64String(contents.getContentsValue());
                tmpBitmap = getBitmapByBase64String(contents.getContentsValue());
                holder.description.setText("image\n");
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.thumbnail.setImageBitmap(tmpBitmap);
                holder.size.setText(Long.toString(contents.getContentsSize()));
                break;
            case Contents.TYPE_FILE:
                holder.description.setText(contents.getContentsValue()+"\n");
                holder.thumbnail.setImageResource(R.drawable.file_icon);
                holder.size.setText(Long.toString(contents.getContentsSize()));
                break;
            case Contents.TYPE_STRING:
                holder.description.setText(contents.getContentsValue());
                holder.thumbnail.setImageResource(R.drawable.text_icon);
                holder.size.setText("-");
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contents contents = contentsList.get(position);

                Log.d("delf", "[SYSTEM] type is " + contents.getContentsType());
                switch (contents.getContentsType()) {

                    case Contents.TYPE_STRING:
                        String copiedString = contents.getContentsValue();
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", copiedString);
                        cm.setPrimaryClip(clip);
                        Toast.makeText(context, R.string.stringAlert, Toast.LENGTH_SHORT).show();

                        break;
                    case Contents.TYPE_IMAGE:
                        new EndpointInBackGround().execute(Message.DOWNLOAD, contents.getContentsPKName());
                        Toast.makeText(context, R.string.imageAlert, Toast.LENGTH_SHORT).show();

                        break;
                    case Contents.TYPE_FILE:
                        new EndpointInBackGround().execute(Message.DOWNLOAD, contents.getContentsPKName());
                        Toast.makeText(context, R.string.fileAlert, Toast.LENGTH_SHORT).show();
                        break;
                }

                progressNoti();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentsList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView sender;
        TextView description;
        TextView time;
        TextView size;

        public HistoryViewHolder(final View historyView) {
            super(historyView);

            thumbnail = (ImageView) historyView.findViewById(R.id.thumbnail);
            sender = (TextView) historyView.findViewById(R.id.contents_sender);
            description = (TextView) historyView.findViewById(R.id.contents_description);
            time = (TextView) historyView.findViewById(R.id.contents_time);
            size = (TextView) historyView.findViewById(R.id.contents_size);
        }
    }

    private Bitmap getBitmapByBase64String(String imageString) {
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private void progressNoti() {
        final NotificationManager mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times


//                        for (incr = 0; incr <= 100; incr+=20) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(0, 0, true);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time

//                            try {
//                                Thread.sleep(5*1000);
//                            } catch (InterruptedException e) {
//                            }
//                        }
                        // When the loop is finished, updates the notification

                        Log.d("choi", "Progress 1");

                        ContentsDownload.DownloadCallback downloadCallback = new ContentsDownload.DownloadCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d("choi", "Progress 2");
                                mBuilder.setContentText("Download complete")
                                        // Removes the progress bar
                                        .setProgress(0,0,false);

                                mNotifyManager.notify(6, mBuilder.build());
                                Log.d("choi", "Progress 3");
                            }
                        };
                        Endpoint.getDownloader().setDownloadCallback(downloadCallback);

                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    private void download() {
        ContentsDownload.DownloadCallback downloadCallback = new ContentsDownload.DownloadCallback() {
            @Override
            public void onSuccess() {

            }
        };
    }
}
