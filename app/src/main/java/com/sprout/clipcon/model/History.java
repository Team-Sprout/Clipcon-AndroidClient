package com.sprout.clipcon.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class History {

//    private Map<String, Contents> contentsMap = new HashMap<String, Contents>();
//
//    /** 새로운 데이터가 업로드되면 히스토리에 add */
//    public void addContents(Contents contents) {
//        contentsMap.put(contents.getContentsPKName(), contents);
//    }
//
//    /** Data를 구분하는 고유키값과 일치하는 Contents를 return */
//    public Contents getContentsByPK(String contentsPKName) {
//        return contentsMap.get(contentsPKName);
//    }

    //// TODO: 2017. 5. 11. have to add Image in Constructor
    public History(String sender, String description) {
        this.sender = sender;
        this.description = description;
    }

    private String sender;
    private String description;
    private String image;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
