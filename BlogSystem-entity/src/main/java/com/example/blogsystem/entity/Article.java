package com.example.blogsystem.entity;

import java.util.Date;

public class Article {
    private String articleid;

    private String userid;

    private String articleName;

    private Date publishTime;

    private Date editTime;

    private String articleContents;

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid == null ? null : articleid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName == null ? null : articleName.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(String articleContents) {
        this.articleContents = articleContents == null ? null : articleContents.trim();
    }
}