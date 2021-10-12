package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.ArticleProviderService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Component;


@Component
public class BackendArticleProviderService implements ArticleProviderService {
    @Override
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("articleContents") String articleContents){
        return "连接超时,请稍后重试!";
    }
}
