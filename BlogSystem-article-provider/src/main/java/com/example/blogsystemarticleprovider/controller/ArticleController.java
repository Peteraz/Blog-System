package com.example.blogsystemarticleprovider.controller;

import java.util.Date;
import javax.annotation.Resource;
import com.alibaba.fastjson.JSONArray;
import com.example.blogsystem.entity.User;
import com.example.blogsystem.entity.Article;
import com.example.blogsystem.common.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.blogsystemarticleprovider.service.ArticleService;

@RestController
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value="createArticle")
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category,@RequestParam("articleContents") String articleContents){
        Article article=new Article();
        User user=new User();
        try{
            user= JSONArray.parseObject(redisTemplate.opsForValue().get("user").toString(),User.class);
            article.setArticleid(UUIDUtils.getId());     //文章id
            article.setUserid(user.getUserid());         //用户id
            article.setArticleName(articleName);         //文章标题
            article.setCategoryName(category);           //文章分类
            article.setArticleContents(articleContents); //文章内容
            article.setPublishTime(new Date());          //文章发表时间
            articleService.insert(article);
            return "1";
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value="getArticle")
    public Article getArticle(@RequestParam("userid") String userid){
        try{
            Article article=articleService.selectByUserId(userid);
            if(article==null){
                return null;
            }else{
                return article;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}