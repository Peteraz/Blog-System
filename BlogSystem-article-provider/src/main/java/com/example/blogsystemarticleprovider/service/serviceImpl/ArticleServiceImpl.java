package com.example.blogsystemarticleprovider.service.serviceImpl;

import com.example.blogsystem.entity.Article;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleService articleService;

    @Override
    public Article selectByPrimaryKey(String articleid){
        return articleService.selectByPrimaryKey(articleid);
    }

    @Override
    public int deleteByPrimaryKey(String articleid){
        return articleService.deleteByPrimaryKey(articleid);
    }

    @Override
    public int insert(Article record){
        return articleService.insert(record);
    }

    @Override
    public int insertSelective(Article record){
        return articleService.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Article record){
        return articleService.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Article record){
        return articleService.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Article record){
        return articleService.updateByPrimaryKey(record);
    }
}
