package com.example.blogsystemconsumer.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
public class PageController {
    @Resource
    private ArticleProviderService articleProviderService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN = "login";

    private static final String ERROR = "error";

    Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping(value = "getLogin")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView(LOGIN);
        modelAndView.addObject("data", "Hello World!!!I ma the Login page!");
        return modelAndView;
    }

    @RequestMapping(value = "getRegister")
    public ModelAndView getRegister() {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "getIndex")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
            return new ModelAndView(LOGIN);
        } else {
            User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            logger.info(user.getUserId());
            modelAndView.addObject("user", user);
            return modelAndView;
        }
    }

    @RequestMapping(value = "getProfile")
    public ModelAndView getProfile() {
        ModelAndView modelAndView = new ModelAndView("profile");
        if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
            return new ModelAndView(LOGIN);
        } else {
            User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            logger.info(user.getUserId());
            modelAndView.addObject("user", user);
            return modelAndView;
        }
    }

    @RequestMapping(value = "getArticleShow")
    public ModelAndView getArticleShow() {
        ModelAndView modelAndView = new ModelAndView("article-show");
        if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
            return new ModelAndView(LOGIN);
        } else {
            User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            logger.info(user.getUserId());
            modelAndView.addObject("user", user).addObject("articleList", articleProviderService.getArticleListById(user.getUserId()));
            return modelAndView;
        }
    }

    @RequestMapping(value = "getForgetPassword")
    public ModelAndView getForgetPassword() {
        return new ModelAndView("forget-password");
    }

    @RequestMapping(value = "getResetPassword")
    public ModelAndView getResetPassword(@RequestParam("token") String token) {
        if (StringUtils.isBlank(token)) {
            return new ModelAndView(ERROR).addObject("message", "不合法访问!");
        }
        if (redisTemplate.getExpire("resetPwdToken") == -2) {
            return new ModelAndView(ERROR).addObject("message", "修改时间已经过了!");
        }
        String resetPwdToken = String.valueOf(redisTemplate.opsForValue().get("resetPwdToken"));
        if (!token.equals(resetPwdToken)) {
            return new ModelAndView(ERROR).addObject("message", "修改码不正确!");
        }
        return new ModelAndView("reset-password");
    }


    @RequestMapping(value = "getArticle")
    public ModelAndView getArticle() {
        ModelAndView modelAndView = new ModelAndView("article");
        if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
            return new ModelAndView(LOGIN);
        } else {
            User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            logger.info(user.getUserId());
            modelAndView.addObject("user", user);
            return modelAndView;
        }
    }

    @RequestMapping(value = "getSettings")
    public ModelAndView getSettings() {
        ModelAndView modelAndView = new ModelAndView("settings");
        if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
            return new ModelAndView(LOGIN);
        } else {
            User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            logger.info(user.getUserId());
            modelAndView.addObject("user", user);
            return modelAndView;
        }
    }

}