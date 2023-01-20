package com.example.blogsystemarticleprovider.service;

import com.example.blogsystem.entity.Article;

import java.util.List;

public interface ArticleService {
    Article selectByPrimaryKey(String articleId);

    Article selectByUserId(String userId);

    List<Article> getArticleListById(String userId);

    int deleteByPrimaryKey(String articleId);

    int insert(Article record);

    int insertSelective(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);
}