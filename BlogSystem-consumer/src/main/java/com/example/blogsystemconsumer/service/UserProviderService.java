package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.hystrix.HystrixUserClientService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.Map;

//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "user-provider-server",path="/user-provider",fallbackFactory = HystrixUserClientService.class)
//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
//@Componet注解最好加上，不加idea会显示有错误，但是不影响系统运行；
@Component
public interface UserProviderService {
    @RequestMapping(value="Register")//注册
    String Register(@RequestBody Map<String,String> map);

    @RequestMapping(value="Login")//登录
    String Login(HttpSession session, @RequestParam("account") String account, @RequestParam("password") String password);

    @RequestMapping(value="Logout")//登录
    String Logout(HttpSession session);

    @RequestMapping(value="ForgetPWD")//登录
    String ForgetPWD(@RequestParam("account") String account);
}