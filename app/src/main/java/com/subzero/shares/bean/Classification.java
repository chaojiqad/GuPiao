package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * Created by fengfeng on 2016/5/9.
 */
public class Classification implements Serializable {

    private String id;
    private String catename;

    public Classification() {

    }

    public Classification(String id, String catename) {
        this.id = id;
        this.catename = catename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }
}

