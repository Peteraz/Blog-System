package com.example.blogsystemarticleprovider.service.serviceImpl;

import com.alibaba.fastjson2.JSONObject;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.UUIDUtils;
import com.example.blogsystem.entity.Article;
import com.example.blogsystem.entity.User;
import com.example.blogsystemarticleprovider.dao.ArticleMapper;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Override
    public Article getArticle(String userId) {
        try {
            Article article = articleMapper.selectByUserId(userId);
            if (article == null) {
                return null;
            } else {
                return article;
            }
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
    public String createArticle(String articleName, String category, String articleContents) {
        Article article = new Article();
        User user = new User();
        try {
            if (!ObjectUtils.isEmpty(redisTemplate.opsForValue().get("user"))) {
                user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                article.setArticleId(UUIDUtils.getId());     //文章id
                article.setUserid(user.getUserId());         //用户id
                article.setArticleName(articleName);         //文章标题
                article.setCategoryName(category);           //文章分类
                article.setArticleContents(articleContents); //文章内容
                article.setPublishTime(new Date());          //文章发表时间
                articleMapper.insert(article);
            } else {
                return null;
            }
            return JsonUtils.jsonPrint(1, "文章发表成功!", null);  //文章发表成功!
        } catch (Exception e) {
            logger.error("create article error: ", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);  //文章发表错误
        }
    }

}
