package com.sprout.clipcon.server;

import android.os.AsyncTask;
import android.util.Log;
import com.sprout.clipcon.model.Message;
import org.json.JSONObject;
import java.io.IOException;
import javax.websocket.EncodeException;

/**
 * Created by delf on 17-05-06.
 */

public class EndpointInBackGround extends AsyncTask<String, Void, String> {

    private BackgroundCallback backgroundCallback;

    public interface BackgroundCallback {
        void onSuccess(JSONObject result);
    }

    public EndpointInBackGround() {
    }

    public EndpointInBackGround(BackgroundCallback callback) {
        this.backgroundCallback = callback;
    }

    @Override
    protected String doInBackground(String... msg) {
        switch (msg[0]) {

            case Message.CONNECT:
                Log.d("delf", "[CLIENT] connecting server...");
                Endpoint.getInstance();
                break;

            case Message.REQUEST_CREATE_GROUP:
                setCallBack();
                sendMessage(
                        new Message().setType(Message.REQUEST_CREATE_GROUP)
                );
                break;

            case Message.REQUEST_JOIN_GROUP:
                setCallBack();
                sendMessage(
                        new Message().setType(Message.REQUEST_JOIN_GROUP)
                                .add(Message.GROUP_PK, msg[1]) // msg[1]: group key
                );
                break;

            case Message.UPLOAD:
                // test code ~
                Log.d("delf", "upload request");
                UploadData tmp = new UploadData("name", "pk");
                tmp.uploadStringData("test string");
                // ~ test code

                break;

            case Message.DOWNLOAD:
                break;

            //...

            default:
                Log.d("delf", "do nothing in doInBackground()");
                break;
        }
        return null;
    }

    private void sendMessage(Message message) {
        try {
            Endpoint.getInstance().sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    private void setCallBack() {
        final Endpoint.SecondCallback secondResult = new Endpoint.SecondCallback() {
            @Override
            public void onSecondSuccess(JSONObject responseFromServer) {
                System.out.println("2차 콜백 성공");
                backgroundCallback.onSuccess(responseFromServer); // call in MainActivity
            }
        };
        Endpoint.getInstance().setSecondCallback(secondResult);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
