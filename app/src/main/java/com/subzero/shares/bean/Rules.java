package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 有奖大赛
 * Created by xzf on 2016/5/23.
 */
public class Rules implements Serializable{

    private String time;
    private String message;
    private String title;
    private String[] images;
    private String id;
    private String website;

    public Rules(){

    }

    public Rules(String time, String message, String title, String[] images, String id, String website) {
        this.time = time;
        this.message = message;
        this.title = title;
        this.images = images;
        this.id = id;
        this.website = website;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
