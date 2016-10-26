package com.subzero.shares.bean;

/**
 * 通知信息
 * Created by xzf on 2016/4/26.
 */
public class UserNotification {

    private String message;


    private String dateAndtime;


    public UserNotification() {


    }

    public UserNotification(String message, String dateAndtime) {

        this.message = message;
        this.dateAndtime = dateAndtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateAndtime() {
        return dateAndtime;
    }

    public void setDateAndtime(String dateAndtime) {
        this.dateAndtime = dateAndtime;
    }
}
