package com.example.blogsystemuserprovider.controller;

import com.example.blogsystem.common.AgeUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.service.UserService;
import com.example.blogsystem.common.SHA256Utils;
import com.example.blogsystem.common.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value = "Register", method = RequestMethod.POST)
    public String Register(@RequestBody Map<String, String> map) {
        User user = new User();
        long count = 0;
        String age="";
        try {
            if (userService.getUserByAccountAndPassword(map.get("account"), null) != null) {
                return "-1";//账号已存在
            } else if (map != null) {
                user.setUserid(UUIDUtils.getUserId());
                user.setAccount(map.get("account"));
                user.setPassword(SHA256Utils.getSHA256((String)map.get("password")));
                user.setUserName(map.get("name"));
                user.setEmail(map.get("email"));
                user.setBirthday(map.get("birthday"));
                age= AgeUtils.getAgeDetail(map.get("birthday"));
                System.out.println(age);
                age=age.substring(0,age.indexOf("岁"));
                user.setAge(Integer.valueOf(age));
                user.setSex(map.get("sex"));
                user.setPhoneNumber(map.get("phone_number"));
                Date time = new Date();
                user.setCreateTime(time);
                user.setLoginCount(count);
                userService.insertSelective(user);
                return "1";//注册成功
            } else {
                return "-2";//注册失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";//注册失败
        }
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password) {
        User user = new User();
        try {
            user = userService.getUserByAccountAndPassword(account, null);
            if (user == null) {
                return "-1";
            } else if (!user.getPassword().equals(SHA256Utils.getSHA256(password))) {
                return "-2";
            } else {
                if (user.getLoginTime() != null) {
                    user.setLastLoginTime(user.getLoginTime());
                }
                //当前登录时间
                user.setLoginTime(new Date());
                //登录次数+1
                user.setLoginCount(user.getLoginCount() + 1);
                //避免暴露密码
                user.setPassword("null");
                redisTemplate.opsForValue().set("user",user.toString());
                System.out.println(redisTemplate.opsForValue().get("user"));
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    public String Logout() {
        //清空用户资料
        try {
            redisTemplate.delete("user");
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    public String ForgetPWD(@RequestParam("email") String email) {
        User user=new User();
        try {
            user=userService.getUserByEmail(email);
            if (user != null) {
                return "1";
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
}