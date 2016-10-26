package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 盘中直播列表
 * Created by zzy on 5/25/2016.
 */
public class OptionalLiveBean implements Serializable {
    private String liveid;
    private String roomid;
    private String uid;
    private String title;
    private String tag;
    private String desc;
    private String state;
    private String avatar;
    private String advisor;
    private String date;
    private String time;

    public OptionalLiveBean(String title, String tag, String roomid, String time) {
        this.title = title;
        this.tag = tag;
        this.roomid = roomid;
        this.time = time;
    }

    public OptionalLiveBean(String liveid, String roomid,String uid,String title, String tag, String desc, String state, String avatar, String advisor, String date, String time) {
        this.liveid = liveid;
        this.roomid = roomid;
        this.uid = uid;
        this.title = title;
        this.tag = tag;
        this.desc = desc;
        this.state = state;
        this.avatar = avatar;
        this.advisor = advisor;
        this.date = date;
        this.time = time;
    }


    public String getUid() {

        return uid;
    }

    public String getRoomid() {
        return roomid;
    }
    public String getLiveid() {
        return liveid;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }
    public String getDesc() {
        return desc;
    }

    public String getState() {
        return state;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
