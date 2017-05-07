package com.sprout.clipcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sprout.clipcon.R;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.Endpoint;
import com.sprout.clipcon.server.EndpointInBackGround;

import java.io.IOException;

import javax.websocket.EncodeException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 연결
        Button createBtn = (Button) findViewById(R.id.main_create);
        Button joinBtn = (Button) findViewById(R.id.main_join);

        // create group
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog();
            }
        });

        // join group
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();
            }
        });

        new EndpointInBackGround().execute("connect");
    }

    public void showCreateDialog() {
        // new EndpointInBackGround().doInBackground(this, "create_group");
        new MaterialDialog.Builder(this)
                .title("그룹명을 입력하세요")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("생성")
                .input("Group 1", "Group 1", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Toast.makeText(getApplicationContext(), "만들어진 그룹명은 " + input.toString() + " 입니다", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                    }
                }).show();


        // way to sending message of request "create group"
        Message req = new Message()
                .setType(Message.REQUEST_CREATE_GROUP) // 1. add type
                .add(Message.GROUP_NAME, "그룹 이름");  // 2. add contents
        try {
            // if exist this reference, use that
            Endpoint.getIntance().sendMessage(req); // 3. send
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    public void showJoinDialog() {
        new MaterialDialog.Builder(this)
                .title("고유키를 입력하세요")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("참여")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Toast.makeText(getApplicationContext(), "고유키는 " + input.toString() + " 입니다", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                    }
                }).show();
    }
}
