package com.sprout.clipcon.server;

import android.util.Log;

import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.model.MessageDecoder;
import com.sprout.clipcon.model.MessageEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
public class Endpoint {

    private String uri = "ws://delf.gonetis.com:8080/websocketServerModule/ServerEndpoint";
     // private String uri = "ws://118.176.16.163:8080/websocketServerModule/ServerEndpoint";
    private Session session;
    private static Endpoint uniqueEndpoint;

    private SecondCallback secondCallback;
    private ParticipantCallback participantCallback;

    // method name recommendation: callBackToWorkThread(), callBackToAsyncTask(), callBackToBackGround()
    public interface SecondCallback {
        // method name recommendation: onResponseToEndpoint()
        void onSecondSuccess(JSONObject result); // define at EndpointInBackground
    }

    // method name recommendation: callBackToFragment()
    public interface ParticipantCallback {
        // method name onServerResponse()
        void onParticipantStatus(String newMemeber, int type); // TODO: 17-05-11 may change String to JSONObject
    }

    public void setSecondCallback(SecondCallback callback) {
        secondCallback = callback;
    }

    public void setParticipantCallback(ParticipantCallback callback) {
        participantCallback = callback;
    }


    public static Endpoint getInstance() {
        try {
            if (uniqueEndpoint == null) {
                uniqueEndpoint = new Endpoint();
            }
        } catch (DeploymentException | IOException | URISyntaxException e) {
            // e.printStackTrace();
            Log.d("delf", "occurred exception");
        }

        return uniqueEndpoint;
    }


    private Endpoint() throws DeploymentException, IOException, URISyntaxException {
        URI uRI = new URI(uri);
        Log.d("delf", "before connect");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);
        Log.d("delf", "after connect");
        System.out.println("************  테스트중 1212 **************");
    }

    @OnOpen
    public void onOpen(Session session) {
        Log.d("delf", "[CLIENT] server connected. session open success.");
        this.session = session;
    }

    @OnMessage
    public void onMessage(Message message) {
        Log.d("delf", "[CLIENT] get message from server: " + message.toString());

        try {
            switch (message.get(Message.TYPE)) {
                case Message.RESPONSE_CREATE_GROUP:
                    switch (message.get(Message.RESULT)) {
                        case Message.CONFIRM:
                            System.out.println("create group confirm");

                            // 2차콜백 성공신호 보내는부분
                            secondCallback.onSecondSuccess(message.getJson());
                            break;

                        case Message.REJECT:

                            System.out.println("create group reject");
                            break;
                    }
                    break;

                case Message.RESPONSE_JOIN_GROUP:

                    switch (message.get(Message.RESULT)) {
                        case Message.CONFIRM:

                            // 2차콜백 성공신호 보내는부분
                            JSONObject response = message.getJson();
                            secondCallback.onSecondSuccess(response);
                            System.out.println("join group confirm");
                            break;
                        case Message.REJECT:
                            System.out.println("join group reject");
                            break;
                    }
                    break;
                case Message.RESPONSE_EXIT_GROUP:
                    Log.d("delf", "[CLIENT] exit the group");
                    break;

                case Message.NOTI_ADD_PARTICIPANT: // 그룹 내 다른 User 들어올 때 마다 Message 받고 UI 갱신
                    participantCallback.onParticipantStatus(message.get(Message.PARTICIPANT_NAME), 1);
                    Log.d("delf", "[CLIENT] \"" + message.get(Message.PARTICIPANT_NAME) + "\" is join in ths group");
                    System.out.println("add participant noti");
                    // TODO: 17-05-10 pass message object to Fragment or Activity
                    break;

                case Message.NOTI_EXIT_PARTICIPANT: // 그룹 내 다른 User 나갈 때 마다 Message 받고 UI 갱신??
                    participantCallback.onParticipantStatus(message.get(Message.PARTICIPANT_NAME), 2);
                    System.out.println("remove participant noti");
                    Log.d("delf", "[CLIENT] \"" + message.get(Message.PARTICIPANT_NAME) + "\" exit the group");
                    break;

                case Message.NOTI_UPLOAD_DATA:
                    Log.d("delf", "[CLIENT] \"" + message.get(Message.NAME) + "\" is upload the data");
                    break;

                default:
                    Log.d("delf", "[CLIENT] unknown message");
                    System.out.println("default");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(Message message) throws IOException, EncodeException {
        if (session == null) {
            System.out.println("debuger_delf: session is null");
        }
        session.getBasicRemote().sendObject(message);

        System.out.println("************  테스트중 1414 **************");
    }

    @OnClose
    public void onClose() {
        // new EndpointInBackGround().execute(Message);
        Log.d("delf", "session closed.");
    }
}