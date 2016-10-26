package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 首页策略咨询
 * Created by zzy on 2016/4/28.
 */
public class ThrenFragmentBean implements Serializable {
    private String id;
    private String title;
    private String website;
    private String time;
    private String image;
    private String message;



    public ThrenFragmentBean(String id, String title, String website, String time, String image, String message) {
        this.id = id;
        this.title = title;
        this.website = website;
        this.time = time;
        this.image = image;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }
}
