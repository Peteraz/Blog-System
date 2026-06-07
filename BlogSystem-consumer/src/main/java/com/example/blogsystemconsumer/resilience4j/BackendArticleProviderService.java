package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystem.entity.Article;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class BackendArticleProviderService implements ArticleProviderService {
    @Override
    public String createArticle(@RequestParam("userId") String userId, @RequestParam("articleName") String articleName, @RequestParam("category") String category, @RequestParam("articleContents") String articleContents) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
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
