package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.*;
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
            userProviderService.Register(map);
            return JsonUtils.jsonPrint(1,"注册成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="tLogin",method= RequestMethod.POST)
    public String Login(HttpSession session, @RequestParam("account") String account,@RequestParam("password") String password){
        try{
            userProviderService.Login(session,account,password);
            return JsonUtils.jsonPrint(1,"登录成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public String Logout(HttpSession session){
        try{
            if(userProviderService.Logout(session).equals("1")){
                return JsonUtils.jsonPrint(1,"登出成功!",null);
            }
            return JsonUtils.jsonPrint(0,"登出失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,e.getMessage(),null);
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