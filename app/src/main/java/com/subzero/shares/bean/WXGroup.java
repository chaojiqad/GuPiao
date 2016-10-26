package com.subzero.shares.bean;

/**
 * 微信群组
 * Created by xzf on 2016/4/29.
 */
public class WXGroup {

    private String[] groups;

    public WXGroup() {

    }

    public WXGroup(String[] groups) {
        this.groups = groups;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }
}
