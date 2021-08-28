package com.example.blogsystemarticleprovider.dao;

import com.example.blogsystem.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
    int deleteByPrimaryKey(String articleid);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(String articleid);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);
}