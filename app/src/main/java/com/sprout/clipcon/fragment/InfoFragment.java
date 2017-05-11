package com.sprout.clipcon.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sprout.clipcon.R;
import com.sprout.clipcon.adapter.MemberAdapter;
import com.sprout.clipcon.model.Member;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;

import org.json.JSONArray;
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

    private ArrayList<Member> membersArrayList = new ArrayList<>();

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
            groupKey = response.get(Message.GROUP_PK).toString();
            nickName = response.get(Message.NAME).toString();

            infoGroupKey.setText(groupKey);
            myNickName.setText(nickName);

            if(response.get(Message.TYPE).equals(Message.RESPONSE_JOIN_GROUP)) {
                JSONArray usersInGroup = response.getJSONArray(Message.LIST);

                for (int i = 0; i < usersInGroup.length(); i++) {
//                    usersInGroup.getJSONObject(i);
                    membersArrayList.add(new Member(usersInGroup.getString(i)));
                    // TODO: 17-05-09 assign in UI
                }
            }
                // TODO: 17-05-09 assign history
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setButtonListener();
        setMemberCallback();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //// TODO: 2017. 5. 11. when Create Group, My nickname is not in Member List. Have to Check it
        memberAdapter = new MemberAdapter(membersArrayList);
        recyclerView.setAdapter(memberAdapter);

        return view;
    }

    private void statusChanged(String newName, int type) {

        if(type == 1){ // add
            membersArrayList.add(new Member(newName));
            System.out.println("추가");
        }else if(type == 2){ // remove
//            membersArrayList.remove();
            System.out.println("삭제");
        }
        memberAdapter = new MemberAdapter(membersArrayList);
        recyclerView.setAdapter(memberAdapter);
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
                changeName();
            }
        });
    }

    private void setMemberCallback() {

        Endpoint.ParticipantCallback participantResult = new Endpoint.ParticipantCallback() {
            @Override
            public void onParticipantStatus(final String newName, final int type) {
                System.out.println("Member List Changed");
                System.out.println("종류는 "+type);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusChanged(newName, type);

                    }
                });
            }
        };
        Endpoint.getInstance().setParticipantCallback(participantResult);
    }

    public void changeName() {
        new MaterialDialog.Builder(getContext())
                .title("새로운 닉네임 입력")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("완료")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence newName) {
                        Toast.makeText(getContext(), newName.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
