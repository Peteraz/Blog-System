package com.example.blogsystemarticleprovider.service.serviceImpl;

import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.UUIDUtils;
import com.example.blogsystem.entity.Article;
import com.example.blogsystemarticleprovider.dao.ArticleMapper;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Override
    public Article getArticle(String userId) {
        try {
            return articleMapper.selectByUserId(userId);
        } catch (Exception e) {
            logger.info("query article error: ", e);
            return null;
        }
    }

    @Override
    public List<Article> getArticleListById(String userId) {
        try {
            List<Article> articleList = articleMapper.getArticleListById(userId);
            logger.info("query article list size: {}", articleList.size());
            return articleList;
        } catch (Exception e) {
            logger.info("query article list error: ", e);
            return Collections.emptyList();
        }
    }

    @Override
    public String createArticle(String userId, String articleName, String category, String articleContents) {
        if (StringUtils.isBlank(userId)) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        try {
            Article article = new Article();
            article.setArticleId(UUIDUtils.getId());
            article.setUserId(userId);
            article.setArticleName(articleName);
            article.setCategoryName(category);
            article.setArticleContents(articleContents);
            article.setPublishTime(new Date());
            articleMapper.insert(article);
            return JsonUtils.jsonPrint(1, "文章发表成功!", null);
        } catch (Exception e) {
            logger.error("create article error: ", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }
}
