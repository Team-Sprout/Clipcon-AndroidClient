package com.sprout.clipcon.server;

import android.os.AsyncTask;

import com.sprout.clipcon.model.Message;

import java.io.IOException;

import javax.websocket.EncodeException;

/**
 * Created by delf on 17-05-06.
 */

public class EndpointInBackGround extends AsyncTask<String, Void, String> {

    private ResultCallback resultCallback;

    public interface ResultCallback {
        public void onSuccess();
    }

    public EndpointInBackGround() {

    }

    public EndpointInBackGround(ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        switch (params[0]) {
            case "connect":
                Endpoint.getInstance();
                System.out.println("************  테스트중 77 **************");
                break;

            case "request_create_group":
                Message req = new Message()
                        .setType(Message.REQUEST_CREATE_GROUP) // 1. add type
                        .add(Message.GROUP_NAME, "그룹 이름");  // 2. add contents

                System.out.println("************  테스트중 88 **************");

                final Endpoint.SecondCallback secondResult = new Endpoint.SecondCallback() {
                    @Override
                    public void onSecondSuccess() {
                        System.out.println("2차 콜백 성공");
                        resultCallback.onSuccess();
                    }
                };
                Endpoint.getInstance().setSecondCallback(secondResult);

                try {
                    // if exist this reference, use that

                    Endpoint.getInstance().sendMessage(req); // 3. send
                    System.out.println("************  테스트중 99 **************");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
                break;

            case "send_message":
                try {
                    Endpoint.getInstance().sendMessage(new Message().setType(Message.REQUEST_CREATE_GROUP));
                    System.out.println("************  테스트중 1010 **************");
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
