package com.example.blogsystemuserprovider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.blogsystem.common.AgeUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.service.UserService;
import com.example.blogsystem.common.SHA256Utils;
import com.example.blogsystem.common.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
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
                return "-1"; //账号已存在
            }else if(userService.getUserByEmail(map.get("email"))!=null){
                return "-2"; //邮箱已使用
            } else if (!map.isEmpty()) {
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
                user.setState(map.get("state"));
                user.setCity(map.get("city"));
                user.setCreateTime(new Date());
                user.setLoginCount(count);
                userService.insertSelective(user);
                return "1";//注册成功
            } else {
                return "-3";//注册失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";//注册失败
        }
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password) {
        User user=new User();
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
                userService.updateByUserSelective(user);
                //避免暴露密码
                user.setPassword("null");
                //redis缓存
                redisTemplate.opsForValue().set("user", JSON.toJSONString(user),7, TimeUnit.DAYS);
                //System.out.println(redisTemplate.opsForValue().get("user"));
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
            return "0";
        }
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String ResetInfo(@RequestBody Map<String, String> map) {
        User user=new User();
        try {
            user=JSONArray.parseObject(redisTemplate.opsForValue().get("user").toString(),User.class);
            if(user==null){
                return "-1";  //用户不存在
            }
            user=userService.getUserById(user.getUserid());
            //user.setAccount(map.get("account"));
            user.setBiography(map.get("biography"));
            user.setUserName(map.get("username"));
            user.setSex(map.get("sex"));
            user.setAge(Integer.valueOf(map.get("age")));
            user.setBirthday(map.get("birthday"));
            user.setPhoneNumber(map.get("phoneNumber"));
            user.setEmail(map.get("email"));
            user.setState(map.get("state"));
            user.setCity(map.get("city"));
            userService.updateByUserSelective(user);
            user.setPassword("null");
            //刷新redis缓存
            redisTemplate.opsForValue().getAndSet("user", JSON.toJSONString(user));
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String ResetPWD(@RequestParam("password") String password,@RequestParam("password1") String password1,@RequestParam("password2") String password2) {
        User user=new User();
        if(!password1.equals(password2)) {
            return "-1"; //两个密码不一样
        }
        try{
            user=JSONArray.parseObject(redisTemplate.opsForValue().get("user").toString(),User.class);
            user=userService.getUserById(user.getUserid());
            if(!SHA256Utils.getSHA256(password).equals(user.getPassword())){
                return "-2"; //原密码不对
            }
            user.setPassword(SHA256Utils.getSHA256(password1));
            userService.updateByUserSelective(user);
            return "1";
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String ResetPassword(@RequestParam("password1") String password1,@RequestParam("password2") String password2) {
        User user=new User();
        if(!password1.equals(password2)) {
            return "-1";      //两个密码不一样
        }
        try{
            if(redisTemplate.getExpire("resetEmail") == -2){
                return "-2"; //修改密码的时候已过期
            }
            String email=redisTemplate.opsForValue().get("resetEmail").toString();
            user=userService.getUserByEmail(email);
            user.setPassword(SHA256Utils.getSHA256(password1));
            userService.updateByUserSelective(user);
            redisTemplate.delete("resetPwdToken");
            redisTemplate.delete("resetEmail");
            return "1";
        }catch(Exception e){
            e.printStackTrace();
            return "0";
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
            return "-1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}