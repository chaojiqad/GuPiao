package com.subzero.shares.bean;

/**
 * Created by lingxia on 5/27/2016.
 */
public class BankBean {
    private String image;
    private String link;
    private String message;

    public BankBean(String image, String message, String link) {
        this.image = image;
        this.message = message;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getMessage() {
        return message;
    }
}
