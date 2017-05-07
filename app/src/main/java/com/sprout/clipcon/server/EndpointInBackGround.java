package com.sprout.clipcon.server;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.sprout.clipcon.model.Message;

import java.io.IOException;

import javax.websocket.EncodeException;

/**
 * Created by delf on 17-05-06.
 */

public class EndpointInBackGround extends AsyncTask<String, Void, String> {
    private AppCompatActivity activity = null;

    public EndpointInBackGround doInBackground(AppCompatActivity iActivity, String type) {
        this.activity = iActivity;
        return this;
    }


    @Override
    protected String doInBackground(String... params) {
        switch (params[0]) {
            case "connect":
                Endpoint.getIntance();
                break;
            case "request_create_group":
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
                break;
            case "send_message":
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
