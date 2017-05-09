package com.sprout.clipcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

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

        new EndpointInBackGround().execute(Message.CONNECT); // connect
    }


    public void showCreateDialog() {
        new MaterialDialog.Builder(this)
                .title("그룹명을 입력하세요")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("생성")
                .input("Group 1", "Group 1", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence inputGroupName) {
                        final String groupName = inputGroupName.toString();

                        // TODO: 17-05-08 show loading screen and block the input until changing screen.
                        final EndpointInBackGround.ResultCallback result = new EndpointInBackGround.ResultCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    startGroupActivity(response, inputGroupName.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        new EndpointInBackGround(result).execute(Message.REQUEST_CREATE_GROUP, groupName);
                    }
                }).show();
    }

    public void showJoinDialog() {
        new MaterialDialog.Builder(this)
                .title("고유키를 입력하세요")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText("참여")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence inputGroupKey) {
                        final EndpointInBackGround.ResultCallback result = new EndpointInBackGround.ResultCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    if (response.get(Message.RESULT).equals(Message.CONFIRM)) {
                                        startGroupActivity(response, inputGroupKey.toString());
                                    } else { // reject
                                        // case: miss matching group key
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

    private void startGroupActivity(JSONObject response, String groupKey) throws JSONException {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
        response.put(Message.GROUP_NAME, groupKey); // add group key to response JSON object
        intent.putExtra("response", response.toString()); // send response to GroupActivity
        startActivity(intent);
    }
}

/*final EndpointInBackGround.ResultCallback resultConnect = new EndpointInBackGround.ResultCallback() {
                            @Override
                            public void onSuccess(String primaryKey) {
                                if(primaryKey.equals(Message.CONFIRM)) {
                                    // send request "create group"
                                }
                                else {
                                    return;
                                }
                            }
                        };
                        new EndpointInBackGround(resultConnect).execute(Message.CONNECT);*/
