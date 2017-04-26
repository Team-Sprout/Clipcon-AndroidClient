package com.sprout.clipcon.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sprout.clipcon.R;
import com.sprout.clipcon.service.MyService;
import com.sprout.clipcon.service.TopService;


/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class InfoFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        String action = getActivity().getIntent().getAction();
        String type = getActivity().getIntent().getType();

        System.out.println("&&&&&&&&&&&&&&&& please1 &&&&&&&&&&&&&&&&");

        // when share image
        if(Intent.ACTION_SEND.equals(action) && type != null){
            Uri uri = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            //TopService.setUri(uri);

            System.out.println("&&&&&&&&&&&&&&&& please2 &&&&&&&&&&&&&&&&");
            try {
                ImageView testImage = (ImageView) view.findViewById(R.id.testImageView);
                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                testImage.setImageBitmap(bm);
                System.out.println("&&&&&&&&&&&&&&&& please3 &&&&&&&&&&&&&&&&");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // insert image uri to clipboard
            ClipData clip = ClipData.newRawUri("test", uri);
            ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(clip);
            System.out.println("&&&&&&&&&&&&&&&& please4 &&&&&&&&&&&&&&&&");
        }

        System.out.println("&&&&&&&&&&&&&&&& please5 &&&&&&&&&&&&&&&&");
        return view;
    }
}
