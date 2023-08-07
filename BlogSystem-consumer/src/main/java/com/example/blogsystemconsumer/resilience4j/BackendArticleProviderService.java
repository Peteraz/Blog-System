package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystem.entity.Article;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class BackendArticleProviderService implements ArticleProviderService {
    @Override
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category, @RequestParam("articleContents") String articleContents) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public Article getArticle(@RequestParam("userid") String userid) {
        return null;
    }

    @Override
    public List<Article> getArticleListById(@RequestParam("userid") String userid) {
        return Collections.emptyList();
    }
}
