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
import android.util.Log;
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
import com.sprout.clipcon.server.EndpointInBackGround;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private MemberAdapter memberAdapter;

    private String groupKey;
    private String nickName;

    private ImageView copyGroupKey;
    private ImageView editNickName;

    private TextView infoGroupKey;
    private TextView myNickName;

    private ArrayList<Member> membersArrayList = new ArrayList<>();

    private static InfoFragment uniqueInfoFragment;

    public static InfoFragment getInstance() {
        if (uniqueInfoFragment == null) {
            uniqueInfoFragment = new InfoFragment();
        }
        return uniqueInfoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        System.out.println("그룹화면으로 진입");

        infoGroupKey = (TextView) view.findViewById(R.id.group_key);
        myNickName = (TextView) view.findViewById(R.id.my_nickname);

        copyGroupKey = (ImageView) view.findViewById(R.id.copyGroupKey);
        editNickName = (ImageView) view.findViewById(R.id.editNickName);


        try {
            JSONObject response = new JSONObject(getActivity().getIntent().getStringExtra("response"));
            groupKey = response.get(Message.GROUP_PK).toString();
            nickName = response.get(Message.NAME).toString();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    infoGroupKey.setText(groupKey);
                    myNickName.setText(nickName);
                }
            });

            JSONArray usersInGroup = response.getJSONArray(Message.LIST);
            for (int i = 0; i < usersInGroup.length(); i++) {
                Log.d("delf", "list loop " + i);
                membersArrayList.add(new Member(usersInGroup.getString(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setButtonListener();
        setMemberCallback();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        memberAdapter = new MemberAdapter(getActivity(), membersArrayList);
        recyclerView.setAdapter(memberAdapter);

        return view;
    }

    // method name recommendation: addParticipant() / addMember() / updateParticipant() / updateMember()
    private void updateMember(String name) {
        Log.d("delf", "[CLIENT] receive name is " + name);

        if (isContain(name)) {
            membersArrayList.remove(getIndex(name));
        } else {
            membersArrayList.add(new Member(name));
        }
        memberAdapter = new MemberAdapter(getActivity(), membersArrayList);
        recyclerView.setAdapter(memberAdapter);
    }

    private void setButtonListener() {
        editNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();

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

    }

    private void showEditDialog() {
        new MaterialDialog.Builder(getContext())
                .title("Input new your new name")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("OK")
                .input(R.string.empty, R.string.empty, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence changedNickname) {
                        new EndpointInBackGround().execute(Message.REQUEST_CHANGE_NAME, changedNickname.toString());
                    }
                }).show();
    }

    private void setMemberCallback() {
        Endpoint.ParticipantCallback participantResult = new Endpoint.ParticipantCallback() {
            @Override
            public void onParticipantStatus(final String newMember) {
                System.out.println("Member List Changed");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMember(newMember);
                    }
                });
            }
        };
        Endpoint.getInstance().setParticipantCallback(participantResult);
    }

    // method name recommendation: showChangeNameDialog()
    public void changeName() {
        Log.d("delf", "[SYSTEM] \"change name\" button clicked");
        //// TODO: 2017. 5. 23. do change Nickname part
    }

    public boolean isContain(String name) {
        Iterator<Member> it = membersArrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getNickname().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(String name) {
        Iterator<Member> it = membersArrayList.iterator();
        Member tmp;
        while (it.hasNext()) {
            tmp = it.next();
            if (tmp.getNickname().equals(name)) {
                return membersArrayList.indexOf(tmp);
            }
        }
        return -1;
    }

    public void changeNickname(final String originName, final String changedName) {
        Log.d("delf", "originName = " + originName + ", changedName = " + changedName);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(myNickName.getText().equals(originName)) {
                    myNickName.setText(changedName);
                }
                for (int i = 0; i < membersArrayList.size(); i++) {
                    if (membersArrayList.get(i).getNickname().equals(originName)) {
                        Member changedMember = membersArrayList.remove(i);
                        changedMember.setName(changedName);
                        membersArrayList.add(i, changedMember);
                        memberAdapter = new MemberAdapter(getActivity(), membersArrayList);
                        recyclerView.setAdapter(memberAdapter);
                        return;
                    }
                }
            }
        });
    }
}
