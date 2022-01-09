package com.example.blogsystemconsumer.controller;

import com.alibaba.fastjson.JSONArray;
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
        try {
            if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
                return new ModelAndView(LOGIN);
            } else {
                User user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                logger.info(user.getUserid());
                modelAndView.addObject("user", user);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value = "getProfile")
    public ModelAndView getProfile() {
        ModelAndView modelAndView = new ModelAndView("profile");
        try {
            if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
                return new ModelAndView(LOGIN);
            } else {
                User user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                logger.info(user.getUserid());
                modelAndView.addObject("user", user);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value = "getArticleShow")
    public ModelAndView getArticleShow() {
        ModelAndView modelAndView = new ModelAndView("article-show");
        try {
            if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
                return new ModelAndView(LOGIN);
            } else {
                User user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                logger.info(user.getUserid());
                modelAndView.addObject("user", user).addObject("articleList", articleProviderService.getArticleListById(user.getUserid()));
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            if (redisTemplate.getExpire("resetPwdToken") == -2) {
                return new ModelAndView(ERROR).addObject("message", "修改时间已经过了!");
            }
            String resetPwdToken = String.valueOf(redisTemplate.opsForValue().get("resetPwdToken"));
            if (!token.equals(resetPwdToken)) {
                return new ModelAndView(ERROR).addObject("message", "修改码不正确!");
            }
            return new ModelAndView("reset-password");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView(ERROR).addObject("message", e.getMessage());
        }
    }


    @RequestMapping(value = "getArticle")
    public ModelAndView getArticle() {
        ModelAndView modelAndView = new ModelAndView("article");
        try {
            if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
                return new ModelAndView(LOGIN);
            } else {
                User user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                logger.info(user.getUserid());
                modelAndView.addObject("user", user);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value = "getSettings")
    public ModelAndView getSettings() {
        ModelAndView modelAndView = new ModelAndView("settings");
        try {
            if (String.valueOf(redisTemplate.opsForValue().get("user")).isEmpty()) {
                return new ModelAndView(LOGIN);
            } else {
                User user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                logger.info(user.getUserid());
                modelAndView.addObject("user", user);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value = "getTest")
    public ModelAndView getTest() {
        return new ModelAndView("test");
    }
}