package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Resource默认按byName自动注入,有两个重要属性分别是name和type
    @Resource
    private UserProviderService userProviderService;

    @RequestMapping(value="getRegister",method= RequestMethod.POST)
    public String getRegister(@RequestParam Map<String,String> map){
        try{
            userProviderService.getRegister(map);
            return JsonUtils.jsonPrint(1,"注册成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="getLogin",method= RequestMethod.POST)
    public String getLogin(HttpServletRequest request, @RequestParam Map<String,String> map){
        try{
            userProviderService.getLogin(request,map);
            return JsonUtils.jsonPrint(1,"登录成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }
}