package com.example.blogsystemarticleprovider.controller;

import com.example.blogsystem.common.UUIDUtils;
import com.example.blogsystem.entity.Article;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@RestController
public class ArticleCotroller {
    @Resource
    private ArticleService articleService;

    @RequestMapping(value="createArticle")
    public String createArticle(String userid,@RequestBody Map<String,String> map){
        Article article=new Article();
        article.setArticleid(UUIDUtils.getId());
        article.setUserid(userid);
        article.setArticleName(map.get("articlename"));
        article.setArticleContents(map.get("articlecontents"));
        Date time=new Date();
        article.setPublishTime(time);
        articleService.insert(article);
        return "";
    }
}
