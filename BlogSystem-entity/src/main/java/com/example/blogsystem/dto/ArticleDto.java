package com.example.blogsystem.dto;

import java.util.Date;

public class ArticleDto {
    private String articleid;

    private String userid;

    private String account;

    private String articleName;

    private Date publishTime;

    private String articleContents;

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(String articleContents) {
        this.articleContents = articleContents;
    }
    @Override
    public String toString() {
        return "ArticleDto{" +
                "articleid='" + articleid + '\'' +
                ", userid='" + userid + '\'' +
                ", author='" + account + '\'' +
                ", articleName='" + articleName + '\'' +
                ", publishTime=" + publishTime +
                ", articleContents='" + articleContents + '\'' +
                '}';
    }
}
