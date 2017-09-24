package com.sprout.clipcon.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.sprout.clipcon.activity.GroupActivity;
import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;
import com.sprout.clipcon.server.EndpointInBackGround;
import com.sprout.clipcon.transfer.RetrofitDownloadData;

import java.text.DecimalFormat;
import java.util.ArrayList;

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
            case Contents.TYPE_STRING:
                holder.description.setText(contents.getContentsValue());
                holder.thumbnail.setImageResource(R.drawable.text_icon);
                holder.size.setText("-");
                break;
            case Contents.TYPE_IMAGE:
                tmpBitmap = getBitmapByBase64String(contents.getContentsValue());
                holder.description.setText("image\n");
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.thumbnail.setImageBitmap(tmpBitmap);
                holder.size.setText(convertContentsSize(contents.getContentsSize()));
                break;
            case Contents.TYPE_FILE:
                holder.description.setText(contents.getContentsValue()+"\n");
                holder.thumbnail.setImageResource(R.drawable.file_icon);
                holder.size.setText(convertContentsSize(contents.getContentsSize()));
                break;

            default:
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contents contents = contentsList.get(position);
                Log.d("delf", "[SYSTEM] type is " + contents.getContentsType());
                new EndpointInBackGround().execute(Message.DOWNLOAD, contents.getContentsPKName());
                switch (contents.getContentsType()) {
                    case Contents.TYPE_STRING:
                        Toast.makeText(context, R.string.stringAlert, Toast.LENGTH_SHORT).show();
                        break;
                    case Contents.TYPE_IMAGE:
                        Toast.makeText(context, R.string.imageAlert, Toast.LENGTH_SHORT).show();
                        break;
                    case Contents.TYPE_FILE:
                        Toast.makeText(context, R.string.fileAlert, Toast.LENGTH_SHORT).show();
                        break;
                }
                showDownloadProgressNoti();
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

    public void showDownloadProgressNoti() {
        final int id = 1;
        final NotificationManager mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("Data Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.icon_logo)
                .setProgress(0, 0, true);

        mNotifyManager.notify(id, mBuilder.build());

        RetrofitDownloadData.DownloadCallback downloadCallback = new RetrofitDownloadData.DownloadCallback() {
            @Override
            public void onDownload(long onGoing, long fileLength, double progressValue) {
                double onGoingMB = ((onGoing / 1024.0) / 1024.0);
                double fileLengthMB = ((fileLength / 1024.0) / 1024.0);

                DecimalFormat dec = new DecimalFormat("0.0");

                String progress = (int) progressValue + "% (" + dec.format(onGoingMB) + " / " + dec.format(fileLengthMB) + " MB)";

                mBuilder.setProgress(100, (int)progressValue, false);
                mBuilder.setContentText("Downloading...  " + progress);
                mBuilder.setAutoCancel(true);

                mNotifyManager.notify(id, mBuilder.build());
            }

            @Override
            public void onComplete() {
                mNotifyManager.cancel(id);

                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("History", "test");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                mBuilder.setProgress(100, 100, false);
                mBuilder.setContentText("Download complete");
                mBuilder.setAutoCancel(true);
                mBuilder.setContentIntent(pendingIntent);

                mNotifyManager.notify(id, mBuilder.build());
            }
        };

        Endpoint.getDownloader().setDownloadCallback(downloadCallback);
    }

    public String convertContentsSize(long size) {
        String contentsConvertedSize;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");
        if (t > 1) {
            contentsConvertedSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            contentsConvertedSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            contentsConvertedSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            contentsConvertedSize = dec.format(k).concat(" KB");
        } else {
            contentsConvertedSize = dec.format(b).concat(" Bytes");
        }
        return contentsConvertedSize;
    }
}
