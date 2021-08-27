package com.example.blogsystemarticleprovider.service;

import com.example.blogsystem.entity.Article;

public interface ArticleService {
    Article selectByPrimaryKey(String articleid);

    int deleteByPrimaryKey(String articleid);

    int insert(Article record);

    int insertSelective(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);
}