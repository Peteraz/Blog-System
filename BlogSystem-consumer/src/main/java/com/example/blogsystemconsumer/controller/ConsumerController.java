package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Resource默认按byName自动注入,有两个重要属性分别是name和type
    @Resource
    private UserProviderService userProviderService;

    @RequestMapping(value="Register",method= RequestMethod.POST)
    public String Register(@RequestBody Map<String,String> map){
        try{
            userProviderService.getRegister(map);
            return JsonUtils.jsonPrint(1,"注册成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Login",method= RequestMethod.POST)
    public String Login(HttpSession session, @RequestParam("account") String account,@RequestParam("password") String password){
        try{
            userProviderService.getLogin(session,account,password);
            return JsonUtils.jsonPrint(1,"登录成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="unLogin",method= RequestMethod.POST)
    public ModelAndView unLogin(HttpSession session){
        try{
            //清空用户资料
            session.setAttribute("user",null);
            return new ModelAndView("index");
        }catch(Exception e){
            e.printStackTrace();
            return new ModelAndView("error");
        }
    }
}