package com.example.blogsystemconsumer.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.blogsystem.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
public class PageController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "getLogin")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("data", "Hello World!!!I ma the Login page!");
        return modelAndView;
    }

    @RequestMapping(value = "getRegister")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }

    @RequestMapping(value = "getIndex")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        String value = redisTemplate.opsForValue().get("user").toString();
        try {
            if (value.isEmpty()) {
                return new ModelAndView("login");
            } else {
                User user = JSONArray.parseObject(value, User.class);
                //System.out.println(user);
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
        String value = redisTemplate.opsForValue().get("user").toString();
        try {
            if (value.isEmpty()) {
                return new ModelAndView("login");
            } else {
                User user = JSONArray.parseObject(value, User.class);
                //System.out.println(user);
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
        String value = redisTemplate.opsForValue().get("user").toString();
        try {
            if (value.isEmpty()) {
                return new ModelAndView("login");
            } else {
                User user = JSONArray.parseObject(value, User.class);
                //System.out.println(user);
                modelAndView.addObject("user", user);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value = "getForgetPassword")
    public ModelAndView getForgetPassword() {
        ModelAndView modelAndView = new ModelAndView("forget-password");
        return modelAndView;
    }

    @RequestMapping(value = "getResetPassword")
    public ModelAndView getResetPassword(@RequestParam("token") String token) {
        if (StringUtils.isBlank(token)) {
            return new ModelAndView("error").addObject("message", "不合法访问!");
        }
        try {
            if (redisTemplate.getExpire("resetPwdToken") == -2) {
                return new ModelAndView("error").addObject("message", "修改时间已经过了!");
            }
            String resetPwdToken = redisTemplate.opsForValue().get("resetPwdToken").toString();
            if (!token.equals(resetPwdToken)) {
                return new ModelAndView("error").addObject("message", "修改码不正确!");
            }
            return new ModelAndView("reset-password");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("error").addObject("message", e.getMessage());
        }
    }


    @RequestMapping(value = "getArticle")
    public ModelAndView getArticle() {
        ModelAndView modelAndView = new ModelAndView("article");
        String value = redisTemplate.opsForValue().get("user").toString();
        try {
            if (value.isEmpty()) {
                return new ModelAndView("login");
            } else {
                User user = JSONArray.parseObject(value, User.class);
                //System.out.println(user);
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
        String value = redisTemplate.opsForValue().get("user").toString();
        try {
            if (value.isEmpty()) {
                return new ModelAndView("login");
            } else {
                User user = JSONArray.parseObject(value, User.class);
                //System.out.println(user);
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
        ModelAndView modelAndView = new ModelAndView("test");
        return modelAndView;
    }
}