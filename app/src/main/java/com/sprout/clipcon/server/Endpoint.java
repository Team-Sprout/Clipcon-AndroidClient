package com.sprout.clipcon.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sprout.clipcon.model.Contents;
import com.sprout.clipcon.model.Group;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.model.MessageDecoder;
import com.sprout.clipcon.model.MessageEncoder;
import com.sprout.clipcon.model.MessageParser;
import com.sprout.clipcon.model.User;

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


    //private String uri = "ws://delf.gonetis.com:8080/websocketServerModule/ServerEndpoint";
    private String uri = "ws://118.176.16.163:8080/websocketServerModule/ServerEndpoint";
    private Session session;
    private static User user;
    //    private static String userName;
//    private static String groupKey;
    private static Endpoint uniqueEndpoint;
    private static ContentsUpload uniqueUploader;
    private static ContentsDownload uniqueDownloader;
    private SecondCallback secondCallback;
    private ParticipantCallback participantCallback;
    private ContentsCallback contentsCallback;

    private Handler handler;

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    public static String lastContentsPK; // [delf] tep field

    public static Endpoint getInstance() {
        try {
            if (uniqueEndpoint == null) {
                uniqueEndpoint = new Endpoint();
            }
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
            Log.d("delf", "occurred exception");
        }
        return uniqueEndpoint;
    }

    public static ContentsUpload getUploader() {
        if (uniqueUploader == null) {
            Log.d("delf", "[SYSTEM] uploader is create. the name is " + user.getName() + " and group key is " + user.getGroup().getPrimaryKey());
            uniqueUploader = new ContentsUpload(user.getName(), user.getGroup().getPrimaryKey());
        }
        return uniqueUploader;
    }

    public static ContentsDownload getDownloader() {
        if (uniqueDownloader == null) {
            uniqueDownloader = new ContentsDownload(user.getName(), user.getGroup().getPrimaryKey());
        }
        return uniqueDownloader;
    }

    // method name recommendation: callBackToWorkThread(), callBackToAsyncTask(), callBackToBackGround()
    public interface SecondCallback {
        void onEndpointResponse(JSONObject result); // define at EndpointInBackground
    }

    public void setSecondCallback(SecondCallback callback) {
        secondCallback = callback;
    }

    // method name recommendation: callBackToFragment()
    public interface ParticipantCallback {
        // method name onServerResponse()
        void onParticipantStatus(String newMemeber);
    }
    public void setParticipantCallback(ParticipantCallback callback) {
        participantCallback = callback;
    }

    public interface ContentsCallback {
        void onContentsUpdate(Contents contents);
    }
    public void setContentsCallback(ContentsCallback callback) {
        contentsCallback = callback;
    }


    private Endpoint() throws DeploymentException, IOException, URISyntaxException {
        URI uRI = new URI(uri);
        Log.d("delf", "[CLIENT] connecting server...");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);

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
                            secondCallback.onEndpointResponse(message.getJson());
                            break;

                        case Message.REJECT:
                            System.out.println("create group reject") ;
                            break;
                    }
                    user = new User(message.get(Message.NAME), new Group(message.get(Message.GROUP_PK)));
                    break;

                case Message.RESPONSE_JOIN_GROUP:
                    secondCallback.onEndpointResponse(message.getJson());
                    switch (message.get(Message.RESULT)) {
                        case Message.CONFIRM:
                            // 2차콜백 성공신호 보내는부분
                            System.out.println("join group confirm");
                            break;

                        case Message.REJECT:
                            System.out.println("join group reject");
                            break;
                    }
                    user = new User(message.get(Message.NAME), new Group(message.get(Message.GROUP_PK)));
                    break;

                case Message.RESPONSE_EXIT_GROUP:
                    Log.d("delf", "[CLIENT] exit the group");
                    break;

                case Message.NOTI_ADD_PARTICIPANT:
                    participantCallback.onParticipantStatus(message.get(Message.PARTICIPANT_NAME));
                    Log.d("delf", "[CLIENT] \"" + message.get(Message.PARTICIPANT_NAME) + "\" is join in ths group");
                    break;

                case Message.NOTI_EXIT_PARTICIPANT:
                    participantCallback.onParticipantStatus(message.get(Message.PARTICIPANT_NAME));
                    System.out.println("remove participant noti");
                    Log.d("delf", "[CLIENT] \"" + message.get(Message.PARTICIPANT_NAME) + "\" exit the group");
                    break;

                case Message.NOTI_UPLOAD_DATA:

                    Log.d("delf", "[CLIENT] \"" + message.get("uploadUserName") + "\" is upload the data");
                    lastContentsPK  = message.get("contentsPKName");
                    Contents contents = MessageParser.getContentsbyMessage(message);
                    user.getGroup().addContents(contents);

                    contentsCallback.onContentsUpdate(contents);
                    if(!message.get("uploadUserName").equals(user.getName())) {
                        handler.sendEmptyMessage(0);
                    }

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

    public void setHandeler(Handler handler) {
        this.handler = handler;
    }

    public void sendMessage(Message message) throws IOException, EncodeException {
        if (session == null) {
            System.out.println("debuger_delf: session is null");
        }
        session.getBasicRemote().sendObject(message);
        Log.d("delf", "[CLIENT] send message to server: " + message.toString());
    }

    @OnClose
    public void onClose() {
        // new EndpointInBackGround().execute(Message.Exit);
        Log.d("delf", "session closed.");
    }

    public static User getUser() {
        return user;
    }
}