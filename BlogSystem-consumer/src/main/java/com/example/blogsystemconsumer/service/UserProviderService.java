package com.example.blogsystemconsumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "user-provider-server",path="/user-provider")
//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
//@Componet注解最好加上，不加idea会显示有错误，但是不影响系统运行；
@Component
public interface UserProviderService {
    @RequestMapping(value="getRegister")//注册
    String getRegister(Map<String,String> map);

    @RequestMapping(value="getLogin")//登录
    String getLogin(HttpServletRequest request,Map<String,String> map);
}