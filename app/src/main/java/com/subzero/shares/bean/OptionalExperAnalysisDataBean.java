package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 顾问问诊二层bean
 * Created by The_p on 2016/5/4.
 */
public class OptionalExperAnalysisDataBean implements Serializable {

    /**
     * name : bob
     * level : 超级顾问
     * glory : 一等奖
     * star : 0
     * avatar :
     * desc : bob出生于亚利桑那州。
     */


    private String id;
    private String user_nicename;
    private String lvname;
    private String glory;
    private String price;
    private String star;
    private String avatar;
    private String desc;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getLvname() {
        return lvname;
    }

    public void setLvname(String lvname) {
        this.lvname = lvname;
    }

    public String getGlory() {
        return glory;
    }

    public void setGlory(String glory) {
        this.glory = glory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

