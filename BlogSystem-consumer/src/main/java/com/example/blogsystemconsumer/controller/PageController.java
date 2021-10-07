package com.example.blogsystemconsumer.controller;

import com.alibaba.fastjson.JSONArray;
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
        ModelAndView modelAndView=new ModelAndView("index");
        String value=redisTemplate.opsForValue().get("user").toString();
        try{
            if(value.isEmpty()){
                return new ModelAndView("login");
            }else{
                User user= JSONArray.parseObject(value,User.class);
                //System.out.println(user);
                modelAndView.addObject("username",user.getUserName());
                return modelAndView;
            }
        }catch(Exception e){
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value="getProfile")
    public ModelAndView getProfile(){
        ModelAndView modelAndView=new ModelAndView("profile");
        String value=redisTemplate.opsForValue().get("user").toString();
        try{
            if(value.isEmpty()){
                return new ModelAndView("login");
            }else{
                User user= JSONArray.parseObject(value,User.class);
                //System.out.println(user);
                modelAndView.addObject("user",user);
                return modelAndView;
            }
        }catch(Exception e){
            e.printStackTrace();
            return modelAndView;
        }
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
        String value=redisTemplate.opsForValue().get("user").toString();
        try{
            if(value.isEmpty()){
                return new ModelAndView("login");
            }else{
                User user= JSONArray.parseObject(value,User.class);
                //System.out.println(user);
                modelAndView.addObject("username",user.getUserName());
                return modelAndView;
            }
        }catch(Exception e){
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping(value="getSettings")
    public ModelAndView getSettings(){
        ModelAndView modelAndView=new ModelAndView("settings");
        String value=redisTemplate.opsForValue().get("user").toString();
        try{
            if(value.isEmpty()){
                return new ModelAndView("login");
            }else{
                User user= JSONArray.parseObject(value,User.class);
                //System.out.println(user);
                modelAndView.addObject("user",user);
                return modelAndView;
            }
        }catch(Exception e){
            e.printStackTrace();
            return modelAndView;
        }
    }
}