package com.sprout.clipcon.server;

import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.model.User;

import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * Created by delf on 17-05-03.
 */

public class Endpoint {
    // private String uri = "ws://182.172.16.118:8080/websocketServerModule/ServerEndpoint";
    // private String uri = "ws://182.172.16.118:8080/websocketServerModule/ServerEndpoint";
    // private String uri = "ws://182.172.16.118:8080/websocketServerModule/ServerEndpoint";
    // private String uri = "ws://223.194.157.244:8080/websocketServerModule/ServerEndpoint";
    private String uri = "ws://211.210.238.157:8080/websocketServerModule/ServerEndpoint";
    private Session session = null;
    private static Endpoint uniqueEndpoint;

    public static User user;

    public static Endpoint getIntance() {
        try {
            if (uniqueEndpoint == null) {
                uniqueEndpoint = new Endpoint();
            }
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return uniqueEndpoint;
    }



    public Endpoint() throws DeploymentException, IOException, URISyntaxException {
        URI uRI = new URI(uri);
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(Message message) throws JSONException {

        System.out.println("message type: " + message.getType());
        switch (message.get(Message.TYPE)) {

            case Message.RESPONSE_CREATE_GROUP:

                switch (message.get(Message.RESULT)) {
                    case Message.CONFIRM:
                        break;
                    case Message.REJECT:
                        break;
                }
                break;

            case Message.RESPONSE_JOIN_GROUP:

                switch (message.get(Message.RESULT)) {
                    case Message.CONFIRM:
                        // processing join group
                        break;
                    case Message.REJECT:
                        // wrong group key
                        break;
                }
                break;

            case Message.RESPONSE_EXIT_GROUP:
                break;

            case Message.NOTI_ADD_PARTICIPANT:
                break;

            case Message.NOTI_EXIT_PARTICIPANT:
                break;

            case Message.NOTI_UPLOAD_DATA:
                break;

            default:
                break;
        }
    }

    public void sendMessage(Message message) throws IOException, EncodeException {
        if (session == null) {
            System.out.println("debuger_delf: session is null");
        }
        session.getBasicRemote().sendObject(message);
    }

    @OnClose
    public void onClose() {
        System.out.println("[debuger_delf] session is closed.");
    }
}