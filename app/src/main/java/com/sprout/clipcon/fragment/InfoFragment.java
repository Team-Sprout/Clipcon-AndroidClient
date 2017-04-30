package com.sprout.clipcon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sprout.clipcon.R;
import com.sprout.clipcon.adapter.MemberAdapter;
import com.sprout.clipcon.model.Member;

import java.util.ArrayList;


/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class InfoFragment extends Fragment {

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    @Override
    public void onAttach(Context context) {
        System.out.println(" InfoFragment ********* onAttach called **********");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println(" InfoFragment ********* onCreate called **********");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        System.out.println(" InfoFragment ********* onCreateView called **********");

        ArrayList<Member> membersArrayList = new ArrayList<>();
        membersArrayList.add(new Member("Member 1"));
        membersArrayList.add(new Member("Member 2"));
        membersArrayList.add(new Member("Member 3"));
        membersArrayList.add(new Member("Member 4"));
        membersArrayList.add(new Member("Member 5"));
        membersArrayList.add(new Member("Member 6"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        memberAdapter = new MemberAdapter(membersArrayList);
        recyclerView.setAdapter(memberAdapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        System.out.println(" InfoFragment ********* onActivityCreated called **********");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {

//        String action = getActivity().getIntent().getAction();
//        String type = getActivity().getIntent().getType();
//
//        // when share image
//        if(Intent.ACTION_SEND.equals(action)){
//
//            startActivity(new Intent(getActivity(), TransparentActivity.class));
//            getActivity().overridePendingTransition(0,0);
//
//            System.out.println("********* ACTION_SEND called *********");
//            Uri uri = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
//            //TopService.setUri(uri);
//
//            try {
//                ImageView testImage = (ImageView) getView().findViewById(R.id.testImageView);
//                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                testImage.setImageBitmap(bm);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // insert image uri to clipboard
//            ClipData clip = ClipData.newRawUri("test", uri);
//            ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//            cm.setPrimaryClip(clip);
//        }


        System.out.println(" InfoFragment ********* onStart called **********");
        super.onStart();
    }

    @Override
    public void onResume() {
        System.out.println(" InfoFragment ********* onResume called **********");
        super.onResume();
    }

    @Override
    public void onPause() {
        System.out.println(" InfoFragment ********* onPause called **********");
        super.onPause();
    }

    @Override
    public void onStop() {
        System.out.println(" InfoFragment ********* onStop called **********");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        System.out.println(" InfoFragment ********* onDestroyView called **********");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        System.out.println(" InfoFragment ********* onDestroy called **********");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        System.out.println(" InfoFragment ********* onDetach called **********");
        super.onDetach();
    }


}
