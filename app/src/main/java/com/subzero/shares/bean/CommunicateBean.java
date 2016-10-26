package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 交流页面Bean
 * Created by zzy on 2016/5/3.
 */
public class CommunicateBean implements Serializable {

    private String useravater;
    private String username;
    private String catename;
    private String time;
    private String content;
    private String replycount;
    private String rewardmoney;
    private String title;
    private String[] images;
    private String id;
    private String userid;


    public CommunicateBean(String id, String useravater, String username, String catename, String time, String content, String replycount, String rewardmoney, String title, String[] images, String userid) {
        this.useravater = useravater;
        this.username = username;
        this.catename = catename;
        this.time = time;
        this.content = content;
        this.replycount = replycount;
        this.rewardmoney = rewardmoney;
        this.title = title;
        this.id = id;
        this.images = images;
        this.userid = userid;
    }



    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public String getUseravater() {
        return useravater;
    }

    public String getUsername() {
        return username;
    }

    public String getCatename() {
        return catename;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getReplycount() {
        return replycount;
    }

    public String getRewardmoney() {
        return rewardmoney;
    }

    public String getTitle() {
        return title;
    }

    public String[] getImages() {
        return images;
    }

    public String getId() {
        return id;
    }
}
