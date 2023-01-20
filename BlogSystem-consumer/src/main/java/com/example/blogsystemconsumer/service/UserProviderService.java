package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.resilience4j.BackendUserProviderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>，它的作用就是实现bean的注入
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "user-provider-server", path = "/user-provider", fallback = BackendUserProviderService.class)
public interface UserProviderService {
    //注册
    @RequestMapping(value = "Register", method = RequestMethod.POST)
    String Register(@RequestBody Map<String, String> map);

    //登录
    @RequestMapping(value = "Login", method = RequestMethod.POST)
    String Login(@RequestParam("account") String account, @RequestParam("password") String password);

    //登出
    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    String Logout();

    //修改用户信息
    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    String ResetInfo(@RequestBody Map<String, String> map);

    //上传头像
    @RequestMapping(value = "IconUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String IconUpload(@RequestPart("file") MultipartFile[] file);

    //修改密码
    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    String ResetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2);

    //重置密码
    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    String ResetPassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2);

    //忘记密码
    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    String ForgetPWD(@RequestParam("email") String email);
}