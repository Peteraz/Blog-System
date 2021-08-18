package com.example.blogsystemuserprovider.controller;

import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.SHA256Utils;
import com.example.blogsystem.common.UserIdUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.service.UserService;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(value="getRegister")
    public String getRegister(Map<String,String> map){
        User user=new User();
        long count=0;
        try{
            user.setUserid(UserIdUtils.getUserId());
            user.setAccount(map.get("account"));
            user.setPassword(SHA256Utils.getSHA256(map.get("password")));
            user.setEmail(map.get("email"));
            user.setName(map.get("name"));
            user.setSex(map.get("sex"));
            Date time = new Date();
            user.setCreateTime(time);
            user.setLoginCount(count);
            userService.insertUser(user);
            return JsonUtils.jsonPrint(1,"成功",null);//注册失败
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);//注册失败
        }
    }

    @RequestMapping(value="getLogin")
    public String getLogin(HttpServletRequest request,Map<String, String> map){
        try{
            User user=new User();
            String account=map.get("account");
            String password=map.get("password");
            user=userService.getUserByAccount(account);
            if(user==null){
                return JsonUtils.jsonPrint(-1,"登账号错误!",null);
            }else if( user.getPassword()!=password){
                return JsonUtils.jsonPrint(0,"密码错误!",null);
            }else{
                //使用request对象的getSession()获取session，如果session不存在则创建一个
                HttpSession session=request.getSession();
                //用session保存用户
                session.setAttribute("user",user);
                return JsonUtils.jsonPrint(1,"登录成功!",null);
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

}
