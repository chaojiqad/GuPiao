package com.subzero.shares.bean;

/**
 * 收支流水
 * Created by xzf on 2016/4/27.
 */
public class AmountOfWater {

    private String from;
    private String time;
    private String type;
    private String money;

    public AmountOfWater() {

    }

    /**
     * 收支流水
     * @param from
     * @param time
     * @param type
     * @param money
     */
    public AmountOfWater(String from, String time, String type, String money) {
        this.from = from;
        this.time = time;
        this.type = type;
        this.money = money;
    }

    /**
     * 收入  支出流水
     * @param from
     * @param time
     * @param money
     */
    public AmountOfWater(String from, String time, String money) {
        this.from = from;
        this.time = time;
        this.money = money;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
