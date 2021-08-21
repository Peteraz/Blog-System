package com.blogsystemconsumer.controller;

import com.blogsystemconsumer.service.UserProviderService;
import com.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Resource默认按byName自动注入,有两个重要属性分别是name和type
    @Resource
    private UserProviderService userProviderService;

    @RequestMapping(value="getRegister",method= RequestMethod.POST)
    public String getRegister(@RequestBody Map<String,String> map){
        try{
            userProviderService.getRegister(map);
            return JsonUtils.jsonPrint(1,"注册成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="getLogin",method= RequestMethod.POST)
    public String getLogin(HttpSession session, @RequestParam("account") String account,@RequestParam("password") String password){
        try{
            userProviderService.getLogin(session,account,password);
            return JsonUtils.jsonPrint(1,"登录成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }
}