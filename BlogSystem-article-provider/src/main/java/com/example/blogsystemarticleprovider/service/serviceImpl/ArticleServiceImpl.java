package com.example.blogsystemarticleprovider.service.serviceImpl;

import com.example.blogsystem.entity.Article;
import com.example.blogsystemarticleprovider.dao.ArticleMapper;
import com.example.blogsystemarticleprovider.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public Article selectByPrimaryKey(String articleId) {
        return articleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public Article selectByUserId(String userId) {
        return articleMapper.selectByUserId(userId);
    }

    @Override
    public List<Article> getArticleListById(String userId) {
        return articleMapper.getArticleListById(userId);
    }

    @Override
    public int deleteByPrimaryKey(String articleId) {
        return articleMapper.deleteByPrimaryKey(articleId);
    }

    @Override
    public int insert(Article record) {
        return articleMapper.insert(record);
    }

    @Override
    public int insertSelective(Article record) {
        return articleMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Article record) {
        return articleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Article record) {
        return articleMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Article record) {
        return articleMapper.updateByPrimaryKey(record);
    }
}
