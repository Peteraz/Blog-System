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
@FeignClient(name = "user-provider-server",path="/user-provider",fallback = BackendUserProviderService.class)
public interface UserProviderService {
    @RequestMapping(value="Register",method = RequestMethod.POST)//注册
    String Register(@RequestBody Map<String, String> map);

    @RequestMapping(value = "Login", method = RequestMethod.POST)//登录
    String Login(@RequestParam("account") String account, @RequestParam("password") String password);

    @RequestMapping(value="Logout",method= RequestMethod.POST)//登出
    String Logout();

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)//修改用户信息
    String ResetInfo(@RequestBody Map<String, String> map);

    @RequestMapping(value="IconUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//上传头像
    String IconUpload(@RequestPart("file") MultipartFile[] file);

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)//修改密码
    String ResetPWD(@RequestParam("password") String password,@RequestParam("password1") String password1,@RequestParam("password2") String password2);

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)//重置密码
    String ResetPassword(@RequestParam("password1") String password1,@RequestParam("password2") String password2);

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)//忘记密码
    String ForgetPWD(@RequestParam("email") String email);
}