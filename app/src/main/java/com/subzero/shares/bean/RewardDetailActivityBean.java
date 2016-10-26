package com.subzero.shares.bean;

/**
 * 悬赏令详情页Bean
 * Created by zzy on 5/4/2016.
 */
public class RewardDetailActivityBean {

    private String replyname;
    private String goods;
    private String replytime;
    private String content;
    private String id;
    private boolean isGoods;

    public RewardDetailActivityBean(String id, String replyname, String goods, String replytime, String content) {
        this.replyname = replyname;
        this.goods = goods;
        this.replytime = replytime;
        this.content = content;
        this.id = id;
    }

    public String getReplyname() {
        return replyname;
    }

    public String getGoods() {
        return goods;
    }

    public String getReplytime() {
        return replytime;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public void setIsGoods(boolean isGoods) {
        this.isGoods = isGoods;
    }

    public boolean isGoods() {
        return isGoods;
    }
}
