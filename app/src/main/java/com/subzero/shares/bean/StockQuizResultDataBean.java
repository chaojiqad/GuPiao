package com.subzero.shares.bean;

import java.io.Serializable;

/**
 * 股指竞猜第二层bean
 * Created by The_p on 2016/4/26.
 */
public class StockQuizResultDataBean implements Serializable {
    private int reds;
    private int greens;
    private String rule;
    private String income;
    private String odds;
    private String todayodds;
    private String tomorrowodds;
    private String wealth;

    public String getWealth() {
        return wealth;
    }

    public void setWealth(String wealth) {
        this.wealth = wealth;
    }
    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public int getGreens() {
        return greens;
    }

    public void setGreens(int greens) {
        this.greens = greens;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        this.reds = reds;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTodayodds() {
        return todayodds;
    }

    public void setTodayodds(String todayodds) {
        this.todayodds = todayodds;
    }

    public String getTomorrowodds() {
        return tomorrowodds;
    }

    public void setTomorrowodds(String tomorrowodds) {
        this.tomorrowodds = tomorrowodds;
    }


}
