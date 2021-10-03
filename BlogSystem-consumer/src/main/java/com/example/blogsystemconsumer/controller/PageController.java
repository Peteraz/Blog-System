package com.example.blogsystemconsumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PageController {
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
        modelAndView.addObject("data","Hello World!!!I ma the index page!");
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