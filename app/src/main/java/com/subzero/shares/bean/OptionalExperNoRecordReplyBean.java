package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 专家收到的回复Bean
 * Created by zzy on 5/12/2016.
 */
public class OptionalExperNoRecordReplyBean implements Serializable{
    private String avatar;
    private String levle;
    private String name;
    private String replyTime;
    private String content;

    public OptionalExperNoRecordReplyBean(String avatar, String levle, String name, String replyTime, String content) {
        this.avatar = avatar;
        this.levle = levle;
        this.name = name;
        this.replyTime = replyTime;
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLevle() {
        return levle;
    }

    public String getName() {
        return name;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public String getContent() {
        return content;
    }
}
