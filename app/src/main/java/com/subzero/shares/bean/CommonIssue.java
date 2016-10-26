package com.subzero.shares.bean;

/**
 * 常见问题
 * Created by xzf on 2016/4/28.
 */
public class CommonIssue {

    private String title;

    private String answer;

    public CommonIssue() {

    }

    public CommonIssue(String title, String answer) {
        this.title = title;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
