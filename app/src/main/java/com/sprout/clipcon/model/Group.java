package com.sprout.clipcon.model;

import java.util.List;

public class Group {
    private String primaryKey;
    private List<User> userList;
    private History history = new History();


    public Group(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void addContents(Contents contents) {
        history.addContents(contents);
    }

    public Contents getContents(String key) {
        return history.getContentsByPK(key);
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public History getHistory() {
        return history;
    }

}