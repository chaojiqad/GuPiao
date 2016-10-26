package com.subzero.shares.bean;

/**
 * 地区
 * Created by xzf on 2016/4/29.
 */
public class Region {

    private String name;
    private String pid;
    private String id;

    public Region(){

    }

    public Region(String name, String pid, String id) {
        this.name = name;
        this.pid = pid;
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
