package com.subzero.shares.bean;

/**
 * 保存群成员的信息
 * Created by xzf on 2016/5/17.
 */
public class UserGroup {

    private String userid;
    private String avatar;
    private String username;
    private String GroupId;

    public UserGroup() {

    }


    public UserGroup(String userid, String avatar, String username, String GroupId) {
        this.userid = userid;
        this.avatar = avatar;
        this.username = username;
        this.GroupId = GroupId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupId() {
        return GroupId;
    }
}
