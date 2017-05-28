package com.sprout.clipcon.model;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class Member {
    private String nickname;
    public Member(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setName(String name) {
        this.nickname = name;
    }
}