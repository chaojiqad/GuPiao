package com.subzero.shares.bean;

/**
 * 群组
 * Created by xzf on 2016/5/16.
 */
public class ChatBean {

    private String groupName;
    private String id;
    private String groupavatar;
    private String membercount;

    public ChatBean() {
    }

    public ChatBean(String id, String groupName,String groupavatar,String membercount) {
        this.id = id;
        this.groupName = groupName;
        this.groupavatar=groupavatar;
        this.membercount=membercount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupavatar(String groupavatar) {
        this.groupavatar = groupavatar;
    }

    public String getGroupavatar() {
        return groupavatar;
    }

    public void setMembercount(String membercount) {
        this.membercount = membercount;
    }

    public String getMembercount() {
        return membercount;
    }
}
