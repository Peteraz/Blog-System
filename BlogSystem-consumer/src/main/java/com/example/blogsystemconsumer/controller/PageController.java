package com.example.blogsystemconsumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PageController {
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
}