package com.example.blogsystem.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {

    private String commentId;

    private String userid;

    private String commentName;

    private String commentContents;

    private Date commentTime;

}