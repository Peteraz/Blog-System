package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.MailProviderService;
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

    @Resource
    private MailProviderService mailProviderService;

    @RequestMapping(value="Register",method= RequestMethod.POST)
        public String Register(@RequestBody Map<String,String> map){
        try{
            if(userProviderService.Register(map).equals("1")){
                return JsonUtils.jsonPrint(1,"注册成功!",null);
            }
            return JsonUtils.jsonPrint(0,"注册失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Login",method= RequestMethod.POST)
    public String Login(HttpSession session, @RequestParam("account") String account,@RequestParam("password") String password){
        try{
            if(userProviderService.Login(session,account,password).equals("1")){
                return JsonUtils.jsonPrint(1,"登录成功!",null);
            }else if(userProviderService.Login(session,account,password).equals("-1")){
                return JsonUtils.jsonPrint(-2,"登录账号错误!",null);
            }else if(userProviderService.Login(session,account,password).equals("-2")){
                return JsonUtils.jsonPrint(-3,"登录密码错误!",null);
            }
            return JsonUtils.jsonPrint(-4,"未知错误!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public ModelAndView Logout(HttpSession session){
        try{
            if(userProviderService.Logout(session).equals("1")){
                return new ModelAndView("index");
            }
            return new ModelAndView("error").addObject("message","登出发生错了");
        }catch(Exception e){
            e.printStackTrace();
            return new ModelAndView("error").addObject("message",e.getMessage());
        }
    }

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)
    public String ForgetPWD(@RequestParam("account") String account){
        try{
            if(userProviderService.ForgetPWD(account).equals("1")){
                mailProviderService.SendMail(account);
                return JsonUtils.jsonPrint(1,"邮件发送成功!",null);
            }
            return JsonUtils.jsonPrint(0,"用户不存在!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,e.getMessage(),null);
        }
    }
}