package com.sprout.clipcon.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.adapter.MemberAdapter;
import com.sprout.clipcon.model.Member;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private MemberAdapter memberAdapter;

    private String groupKey;
    private String nickName;

    private ImageView copyGroupKey ;
    private ImageView editNickName ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        System.out.println("그룹화면으로 진입");

        TextView infoGroupKey = (TextView) view.findViewById(R.id.group_key);
        TextView myNickName = (TextView) view.findViewById(R.id.my_nickname);

        copyGroupKey = (ImageView) view.findViewById(R.id.copyGroupKey);
        editNickName = (ImageView) view.findViewById(R.id.editNickName);

        try {
            JSONObject response = new JSONObject(getActivity().getIntent().getStringExtra("response"));
            System.out.println("리스폰스" +response);
            groupKey = response.get(Message.GROUP_PK).toString();
            nickName = response.get(Message.NAME).toString();

            System.out.println("여기다");
            System.out.println(groupKey);
            System.out.println(nickName);
            System.out.println("여기다 끝");

            infoGroupKey.setText(groupKey);
            myNickName.setText(nickName);

                // TODO: 17-05-09 assign history
        } catch (JSONException e) {
            System.out.println("여기냐?");
            e.printStackTrace();
        }

        setButtonListener();
        setJoinCallback();

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

    private void setButtonListener() {

        copyGroupKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Test", groupKey);
                cm.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Copy Key", Toast.LENGTH_SHORT).show();
            }
        });

        editNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit NickName", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setJoinCallback() {

        Endpoint.JoinCallback joinResult = new Endpoint.JoinCallback() {
            @Override
            public void onJoinAdded() {
                System.out.println("New Member Joined");
            }
        };
        Endpoint.getInstance().setJoinCallback(joinResult);
    }
}
