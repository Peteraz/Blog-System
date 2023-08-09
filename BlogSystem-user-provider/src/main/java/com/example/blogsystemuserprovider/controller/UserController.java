package com.example.blogsystemuserprovider.controller;

import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "Register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String login(@RequestParam("account") String account, @RequestParam("password") String password) {
        return userService.login(account, password);
    }

    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    public String logout() {
        return userService.logout();
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String resetInfo(@RequestBody Map<String, String> map) {
        return userService.resetInfo(map);
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String resetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return userService.resetPWD(password, password1, password2);
    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return userService.resetPassword(password1, password2);
    }

    @RequestMapping(value = "IconUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String iconUpload(@RequestPart("file") MultipartFile[] file) {
        return userService.iconUpload(file);
    }

    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    public String forgetPWD(@RequestParam("email") String email) {
        return userService.forgetPWD(email);
    }
}