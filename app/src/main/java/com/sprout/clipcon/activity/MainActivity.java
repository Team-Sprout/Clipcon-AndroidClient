package com.sprout.clipcon.activity;

import android.content.Context;
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
import com.sprout.clipcon.server.EndpointInBackGround;

import org.json.JSONException;
import org.json.JSONObject;

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

                final EndpointInBackGround.BackgroundCallback result = new EndpointInBackGround.BackgroundCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            startGroupActivity(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new EndpointInBackGround(result).execute(Message.REQUEST_CREATE_GROUP);
            }
        });
        // join group
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();
            }
        });


    }

    public void showJoinDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.inputKey)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText(R.string.joinKo)
                .input(R.string.empty, R.string.empty, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence inputGroupKey) {
                        final EndpointInBackGround.BackgroundCallback result = new EndpointInBackGround.BackgroundCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    if (response.get(Message.RESULT).equals(Message.CONFIRM)) {
                                        response.put(Message.GROUP_NAME, inputGroupKey.toString());
                                        System.out.println("결과는 성공" + response.get(Message.RESULT));
                                        startGroupActivity(response);
                                    } else { // reject
                                        //// TODO: 2017. 5. 12. have to put Toast Message
                                        System.out.println("결과는 실패" + response.get(Message.RESULT));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        new EndpointInBackGround(result).execute(Message.REQUEST_JOIN_GROUP, inputGroupKey.toString());
                    }
                }).show();
    }

    private void startGroupActivity(JSONObject response) throws JSONException {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);

        intent.putExtra("response", response.toString()); // send response to GroupActivity
        startActivity(intent);
    }
}
