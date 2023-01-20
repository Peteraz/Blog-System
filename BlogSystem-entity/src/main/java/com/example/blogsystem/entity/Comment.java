package com.example.blogsystem.entity;

import java.util.Date;

public class Comment {
    private String commentId;

    private String userid;

    private String commentName;

    private String commentContents;

    private Date commentTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId == null ? null : commentId.trim();
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

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", userid='" + userid + '\'' +
                ", commentName='" + commentName + '\'' +
                ", commentContents='" + commentContents + '\'' +
                ", commentTime=" + commentTime +
                '}';
    }
}