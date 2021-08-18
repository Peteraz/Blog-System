package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Autowired是用在JavaBean中的注解，通过byType形式，用来给指定的字段或方法注入所需的外部资源。@Autowired是优先根据类型装配。
    @Resource
    private UserProviderService userProviderService;

    @RequestMapping(value="getRegister")
    public String getRegister(@RequestBody Map<String,String> map){
        try{
            userProviderService.getRegister(map);
            return JsonUtils.jsonPrint(1,"注册成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="getLogin")
    public String getLogin(HttpServletRequest request,@RequestBody Map<String,String> map){
        try{
            userProviderService.getLogin(request,map);
            return JsonUtils.jsonPrint(1,"登录成功",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }
}