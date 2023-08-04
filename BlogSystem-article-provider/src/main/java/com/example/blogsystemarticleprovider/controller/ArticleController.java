package com.example.blogsystemarticleprovider.controller;

import com.example.blogsystem.entity.Article;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @RequestMapping(value = "createArticle")
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category, @RequestParam("articleContents") String articleContents) {
        return articleService.createArticle(articleName, category, articleContents);
    }

    @RequestMapping(value = "getArticle")
    public Article getArticle(@RequestParam("userId") String userId) {
        return articleService.getArticle(userId);

    }

    @RequestMapping(value = "getArticleListById")
    public List<Article> getArticleListById(@RequestParam("userId") String userId) {
        return articleService.getArticleListById(userId);
    }

}