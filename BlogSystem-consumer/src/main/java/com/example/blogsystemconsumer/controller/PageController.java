package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.UserProviderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;

@RestController
public class PageController {
    @Resource
    private UserProviderService userProviderService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value="getLogin")
    public ModelAndView getLogin(){
        ModelAndView modelAndView=new ModelAndView("login");
        modelAndView.addObject("data","Hello World!!!I ma the Login page!");
        return modelAndView;
    }

    @RequestMapping(value="getRegister")
    public ModelAndView getRegister(){
        ModelAndView modelAndView=new ModelAndView("register");
        modelAndView.addObject("data","Hello World!!!I ma the Login page!");
        return modelAndView;
    }
    @RequestMapping(value="getIndex")
    public ModelAndView getIndex(){
        User user=new User();
        ModelAndView modelAndView=new ModelAndView("index");
        try{
            user=(User)redisTemplate.opsForHash().get("user","1");
            modelAndView.addObject("user",user);
        }catch(Exception e){
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value="getProfile")
    public ModelAndView getProfile(){
        ModelAndView modelAndView=new ModelAndView("profile");
        modelAndView.addObject("data","Hello World!!!I ma the profile page!");
        return modelAndView;
    }

    @RequestMapping(value="getResetPassword")
    public ModelAndView getResetPassword(){
        ModelAndView modelAndView=new ModelAndView("reset-password");
        modelAndView.addObject("data","Hello World!!!I ma the ResetPassword page!");
        return modelAndView;
    }

    @RequestMapping(value="getArticle")
    public ModelAndView getArticle(){
        ModelAndView modelAndView=new ModelAndView("article");
        modelAndView.addObject("data","Hello World!!!I ma the article page!");
        return modelAndView;
    }

    @RequestMapping(value="getSettings")
    public ModelAndView getSettings(){
        ModelAndView modelAndView=new ModelAndView("settings");
        modelAndView.addObject("data","Hello World!!!I ma the setting page!");
        return modelAndView;
    }
}