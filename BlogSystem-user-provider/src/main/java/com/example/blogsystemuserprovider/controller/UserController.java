package com.example.blogsystemuserprovider.controller;

import com.example.blogsystemuserprovider.service.UserService;
import com.example.blogsystem.entity.User;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.SHA256Utils;
import com.example.blogsystem.common.UUIDUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(value="Register",method= RequestMethod.POST)
    public String Register(@RequestBody  Map<String,String> map){
        User user=new User();
        long count=0;
        try{
              if(userService.getUserByAccountAndPassword(map.get("account"),null)!=null){
                  return JsonUtils.jsonPrint(-1,"用户已存在",null);//账号已存在
              }else if(map!=null){
                  user.setUserid(UUIDUtils.getUserId());
                  user.setAccount(map.get("account"));
                  user.setPassword(SHA256Utils.getSHA256(map.get("password")));
                  user.setEmail(map.get("email"));
                  user.setUserName(map.get("name"));
                  user.setAge(Integer.getInteger(map.get("age")));
                  user.setSex(map.get("sex"));
                  Date time = new Date();
                  user.setCreateTime(time);
                  user.setLoginCount(count);
                  userService.insertSelective(user);
                  return "1";//注册成功
              }else{
                  return "0";//注册失败
              }
        }catch(Exception e){
            e.printStackTrace();
            return "-1";//注册失败
        }
    }

    @RequestMapping(value="Login",method= RequestMethod.GET)
    public String Login(HttpSession session, @RequestParam("account") String account, @RequestParam("password") String password){
        User user=new User();
        try{
            user=userService.getUserByAccountAndPassword(account,null);
            if(user==null){
                return "-2";
            }else if( user.getPassword().equals(SHA256Utils.getSHA256(password))){
                return "-3";
            }else{
                if(user.getLoginTime()!=null){
                    user.setLastLoginTime(user.getLoginTime());
                }
                //当前登录时间
                user.setLoginTime(new Date());
                //登录次数+1
                user.setLoginCount(user.getLoginCount()+1);
                //避免暴露密码
                user.setPassword("null");
                //用session保存用户
                session.setAttribute("user",user);
                return "1";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public String Logout(HttpSession session){
        try{
            //清空用户资料
            session.setAttribute("user",null);
            return "1";
        }catch(Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)
    public String ForgetPWD(@RequestParam("account") String account) {
        try {
            if (userService.getUserByAccountAndPassword(account, null) != null) {
                return "1";
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
}
