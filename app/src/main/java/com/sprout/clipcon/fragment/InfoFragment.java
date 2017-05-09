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
    private String groupName;

    private ImageView editGroupName;
    private ImageView copyGroupKey ;
    private ImageView editNickName ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        System.out.println("그룹화면으로 진입");

        TextView infoGroupName = (TextView) view.findViewById(R.id.group_name);
        TextView infoGroupKey = (TextView) view.findViewById(R.id.group_key);

        editGroupName = (ImageView) view.findViewById(R.id.editGroupName);
        copyGroupKey = (ImageView) view.findViewById(R.id.copyGroupKey);
        editNickName = (ImageView) view.findViewById(R.id.editNickName);
        // String groupName = getActivity().getIntent().getStringExtra("name");

        try {
            JSONObject response = new JSONObject(getActivity().getIntent().getStringExtra("response"));

            groupName = response.get(Message.GROUP_NAME).toString();
            groupKey = response.get(Message.GROUP_PK).toString();

            infoGroupName.setText(groupName);
            infoGroupKey.setText(groupKey);

            /*if(response.get(Message.TYPE).equals(Message.REQUEST_JOIN_GROUP)) {
                JSONArray usersInGroup = response.getJSONArray(Message.LIST);
                Iterator<?> it = usersInGroup.iterator();
                while (it.hasNext()) {
                    String tmpString = (String) it.next();
                    userStringList.add(tmpString);
                }

            }*/
                // TODO: 17-05-09 assign history
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setButtonListener();

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
        editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit Group Name", Toast.LENGTH_SHORT).show();
            }
        });

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
}
