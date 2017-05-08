package com.sprout.clipcon.server;

import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AppCompatActivity;

import com.sprout.clipcon.model.Message;

import java.io.IOException;

import javax.websocket.EncodeException;

/**
 * Created by delf on 17-05-06.
 */

public class EndpointInBackGround extends AsyncTask<String, Void, String> {
<<<<<<< HEAD
    private AppCompatActivity activity = null;
=======
//    private AppCompatActivity  activity = null;
//    public EndpointInBackGround doInBackground(AppCompatActivity iActivity, String type) {
//        this.activity = iActivity;
//        return this;
//    }
>>>>>>> 3c6425d6342629615fc632488c57e48b8de8ecb2

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
                Endpoint.getIntance();
                System.out.println("************  테스트중 77 **************");
                break;

            case "request_create_group":
                Message req = new Message()
                        .setType(Message.REQUEST_CREATE_GROUP) // 1. add type
                        .add(Message.GROUP_NAME, "그룹 이름");  // 2. add contents
<<<<<<< HEAD
                try {
                    // if exist this reference, use that
                    Endpoint.getIntance().sendMessage(req); // 3. send
=======

                System.out.println("************  테스트중 88 **************");

                //// TODO: 2017. 5. 8. ( EndpointInBackGround <-> Endpoint ) 2차콜백 부분.
                // 2차 콜백. Endpoint의 onMessage에서 RESPONSE_CREATE_GROUP 부르고 Message.CONFIRM 호출되면 성공.
                // 호출성공 여부를 MainActivity로 알려줌 -> 호출 성공에 따른 그룹화면 진입을 위해
                // 아직 미구현

                final Endpoint.SecondCallback secondResult = new Endpoint.SecondCallback() {
                    @Override
                    public void onSecondSuccess() {
                        resultCallback.onSuccess();
                    }
                };
                //secondResult를 Endpoint로 넘기려면 Endpoint를 AsyncTask로 구현해야 함


                try {
                    // if exist this reference, use that

                    Endpoint.getIntance().sendMessage(req); // 3. send

                    // 1차콜백 성공신호 보내는 부분. (임시로 여기다 뒀음, 2차 콜백 결과에 따라 MainActivity로 날려야 함)
                    resultCallback.onSuccess();

                    System.out.println("************  테스트중 99 **************");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
                break;

            case "send_message":
                try {
                    Endpoint.getIntance().sendMessage(new Message().setType(Message.REQUEST_CREATE_GROUP));
                    System.out.println("************  테스트중 1010 **************");
>>>>>>> 3c6425d6342629615fc632488c57e48b8de8ecb2
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
