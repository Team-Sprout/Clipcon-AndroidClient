package com.sprout.clipcon.model;

import org.json.JSONException;
import org.json.JSONObject;

/*@Setter
@Getter
@NoArgsConstructor*/
public class Message {
    private String type;
    private JSONObject json;

    public Message() {}

    public String getType() {
        return type;
    }

    public JSONObject getJson() {
        return json;
    }

    public Message setJson(String string) {
        try {
            json = new JSONObject(string);

            type = json.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Message setJson(JSONObject json) {
        try {
            this.json = json;
            type = json.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Message setType(String type) {
        try {
            json = new JSONObject();
            this.type = type;
            json.put(TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Message(String type, String jsonString) {
        try {
            this.type = type;
            json = new JSONObject();
            json.put(TYPE, type);
            json.put(CONTENTS, jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json.toString());
    }

    public Message add(String key, String value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
 // 테스트 해보겠다
    public String get(String key) throws JSONException {
        return json.get(key).toString();
    }

    public String toString() {
        return json.toString();
    }

    public Object getObject(String key) throws JSONException {
        return json.get(key);
    }

    public long getLong(String key) throws JSONException {
        return json.getLong(key);
    }
    public final static String TYPE = "message type";

    public final static String REQUEST_CREATE_GROUP = "request/create group";
    public final static String REQUEST_JOIN_GROUP = "request/join group";
    public final static String REQUEST_EXIT_GROUP = "request/exit group";
    public final static String REQUEST_TEST = "request/test";

    public final static String RESPONSE_CREATE_GROUP = "response/create group";
    public final static String RESPONSE_JOIN_GROUP = "response/join group";
    public final static String RESPONSE_EXIT_GROUP = "response/exit group";

    public final static String NOTI_ADD_PARTICIPANT = "noti/add participant";
    public final static String NOTI_EXIT_PARTICIPANT = "noti/exit participant";
    public final static String NOTI_UPLOAD_DATA = "noti/upload data";

    public final static String RESULT = "result";
    public final static String CONFIRM = "confirm";
    public final static String REJECT = "reject";

    public final static String NAME = "name";
    public final static String CONTENTS = "contents";
    public final static String LIST = "list";
    public final static String USER_INFO = "user information";
    public final static String GROUP_NAME = "group name";
    public final static String GROUP_PK = "group pk";
    public final static String GROUP_INFO = "group information";
    public final static String PARTICIPANT_NAME = "participant name";

    public final static String TEST_DEBUG_MODE = "debug";
}
