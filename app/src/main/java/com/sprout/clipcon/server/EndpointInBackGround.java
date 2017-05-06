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
    private AppCompatActivity  activity = null;

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
                break;
            case "send_message":
                try {
                    Endpoint.getIntance().sendMessage(new Message().setType(Message.REQUEST_CREATE_GROUP));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
