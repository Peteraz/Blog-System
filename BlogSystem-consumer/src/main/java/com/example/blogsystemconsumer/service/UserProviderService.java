package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.resilience4j.BackendUserProviderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.Map;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>，它的作用就是实现bean的注入
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "user-provider-server",path="/user-provider",fallback = BackendUserProviderService.class)
public interface UserProviderService {
    @RequestMapping(value="Register",method = RequestMethod.POST)//注册
    String Register(@RequestBody Map<String, String> map);

    @RequestMapping(value = "Login", method = RequestMethod.POST)//登录
    String Login(@RequestParam("account") String account, @RequestParam("password") String password);

    @RequestMapping(value="Logout",method= RequestMethod.POST)//登出
    String Logout(HttpSession session);

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)//忘记密码
    String ForgetPWD(@RequestParam("account") String account);
}