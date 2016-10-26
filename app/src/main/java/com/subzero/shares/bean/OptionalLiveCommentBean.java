package com.subzero.shares.bean;

/**
 * 直播收到的评论
 * Created by zzy on 5/25/2016.
 */
public class OptionalLiveCommentBean {
    private String comName;
    private String time;
    private String content;

    public OptionalLiveCommentBean(String comName, String time, String content) {
        this.comName = comName;
        this.time = time;
        this.content = content;
    }

    public String getComName() {
        return comName;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
