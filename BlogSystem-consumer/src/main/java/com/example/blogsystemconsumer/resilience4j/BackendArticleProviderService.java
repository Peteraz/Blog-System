package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystem.entity.Article;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Component;

@Component
public class BackendArticleProviderService implements ArticleProviderService {
    @Override
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category,@RequestParam("articleContents") String articleContents){
        return "连接超时,请稍后重试!";
    }

    @Override
    public Article getArticle(@RequestParam("userid") String userid){
        Article article=null;
        return article;
    }
}
