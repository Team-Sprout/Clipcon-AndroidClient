package com.sprout.clipcon.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.sprout.clipcon.server.EndpointInBackGround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
                holder.description.setText(contents.getContentsValue());
                holder.thumbnail.setImageResource(R.drawable.file_icon);
                holder.size.setText((int) contents.getContentsSize());
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
                Toast.makeText(context, position + "번째가 클릭됐음", Toast.LENGTH_SHORT).show();

                Contents contents = contentsList.get(position);

                if (contents.getContentsType().equals(Contents.TYPE_STRING)) {
                    String copiedString = contents.getContentsValue();
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", copiedString);
                    cm.setPrimaryClip(clip);
                } else if (contents.getContentsType().equals(Contents.TYPE_IMAGE)) {
                    Log.d("delf", "[SYSTEM] type is " + contents.getContentsType());
                    new EndpointInBackGround().execute(Message.DOWNLOAD, contents.getContentsPKName());

//                    MediaStore.Images.Media.insertImage(context.getContentResolver(), tmpBitmap, "title", "description");
                    imageToGallery(tmpBitmap);
                }
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

    private void imageToGallery(Bitmap testBitmap) {

        OutputStream fOut = null;
        String fileName = "Image" + createName(System.currentTimeMillis()) + ".png";
        final String appDirectoryName = "Clipcon";
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
