package com.subzero.shares.bean;

/**
 * 用户
 * Created by xzf on 2016/4/25.
 */
public class User {
    private String level;
    private String avatar;

    private String phone;
    private String name;
    private String email;
    private String wxgroup;
    private String wxid;
    private String sex;
    private String birthday;
    private String shareage;
    private String city;
    private String industry;

    public User() {
    }


    public User(String avatar, String level, String phone, String name, String email, String wxgroup, String wxid, String sex, String birthday, String shareage, String city, String industry) {
        this.level = level;
        this.avatar = avatar;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.wxgroup = wxgroup;
        this.wxid = wxid;
        this.sex = sex;
        this.birthday = birthday;
        this.shareage = shareage;
        this.city = city;
        this.industry = industry;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWxgroup() {
        return wxgroup;
    }

    public void setWxgroup(String wxgroup) {
        this.wxgroup = wxgroup;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getShareage() {
        return shareage;
    }

    public void setShareage(String shareage) {
        this.shareage = shareage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
