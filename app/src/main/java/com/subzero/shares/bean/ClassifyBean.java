package com.subzero.shares.bean;

/**
 * 课程类别bean
 * Created by zzy on 5/16/2016.
 */
public class ClassifyBean {
    private String name;
    private String id;

    private boolean isSelect;

    public ClassifyBean(String name, String id) {

        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
