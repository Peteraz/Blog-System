package com.example.blogsystem.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Article {

    private String articleId;

    private String userid;

    private String articleName;

    private String categoryName;

    private Date publishTime;

    private Date editTime;

    private String articleContents;

}