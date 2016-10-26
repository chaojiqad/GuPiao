package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 顾问诊股————专家
 * Created by zzy on 5/11/2016.
 */
public class OptionalExperNoRecordBean implements Serializable {
    private String title;
    private String message;
    private String time;
    private String[] image;
    private String id;

    public OptionalExperNoRecordBean(String id, String title, String message, String time, String[] image) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.image = image;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String[] getImage() {
        return image;
    }

    public String getId() {
        return id;
    }
}
