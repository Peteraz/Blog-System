package com.blogsystem.entity;

import java.util.Date;

public class Comment {
    private String commentid;

    private String userid;

    private String commentName;

    private String commentContents;

    private Date commentTimne;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid == null ? null : commentid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName == null ? null : commentName.trim();
    }

    public String getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(String commentContents) {
        this.commentContents = commentContents == null ? null : commentContents.trim();
    }

    public Date getCommentTimne() {
        return commentTimne;
    }

    public void setCommentTimne(Date commentTimne) {
        this.commentTimne = commentTimne;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentid='" + commentid + '\'' +
                ", userid='" + userid + '\'' +
                ", commentName='" + commentName + '\'' +
                ", commentContents='" + commentContents + '\'' +
                ", commentTimne=" + commentTimne +
                '}';
    }
}