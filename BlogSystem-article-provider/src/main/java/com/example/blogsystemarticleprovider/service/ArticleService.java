package com.example.blogsystemarticleprovider.service;

import com.example.blogsystem.entity.Article;

import java.util.List;

public interface ArticleService {

    Article getArticle(String userId);

    List<Article> getArticleListById(String userId);

    String createArticle(String articleName, String category, String articleContents);

}