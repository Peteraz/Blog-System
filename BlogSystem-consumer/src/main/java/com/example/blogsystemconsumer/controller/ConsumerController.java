package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.ProductService;
import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Resource默认按byName自动注入,有两个重要属性分别是name和type
    @Resource
    private ProductService productService;

    @Resource
    private UserProviderService userProviderService;

    @Resource
    private MailProviderService mailProviderService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value="getConsumer")
    public String getConsumer(){
        String str=productService.getService();
        return str;
    }

    @RequestMapping(value="Register",method = RequestMethod.POST)
    public String Register(@RequestBody Map<String, String> map){
        if(map.isEmpty()) {
            return JsonUtils.jsonPrint(-3,"收不到注册信息!",null);
        }
        try{
            String result=userProviderService.Register(map);
            System.out.println(result);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"注册成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"用户名已存在!",null);
            }
            return JsonUtils.jsonPrint(-2,"注册失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password){
        User user=new User();
        if(account.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入账号!",null);
        }else if(password.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入密码!",null);
        }
        try{
            String result=userProviderService.Login(account,password);
            //System.out.println(result);
            switch (result) {
                case "1":
                    return JsonUtils.jsonPrint(1, "登录成功!", null);
                case "-1":
                    return JsonUtils.jsonPrint(-1, "登录账号错误!", null);
                case "-2":
                    return JsonUtils.jsonPrint(-1, "登录密码错误!", null);
                default: return JsonUtils.jsonPrint(-3,result,null);
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public String Logout(){
        try{
            String result=userProviderService.Logout();
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"登出成功!",null);
            }
            return JsonUtils.jsonPrint(-1,"登出发生错了!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String ResetInfo(@RequestBody Map<String, String> map){
        if(map.isEmpty()){
            return JsonUtils.jsonPrint(-3,"接收不到数据!",null);
        }
        try{
            String result=userProviderService.ResetInfo(map);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"修改成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"修改失败!",null);
            }else if(result.equals("-2")){
                return JsonUtils.jsonPrint(-2,"修改失败!",null);
            }
            return JsonUtils.jsonPrint(-3,"未知错误!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String ResetPWD(@RequestParam("password") String password){
        if(password.isEmpty()){
            return JsonUtils.jsonPrint(-3,"",null);
        }
        try{
            String result=userProviderService.ResetPWD(password);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"密码修改成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"密码修改失败!",null);
            }
            return JsonUtils.jsonPrint(-2,"未知错误!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)
    public String ForgetPWD(@RequestParam("account") String account){
        try{
            if(userProviderService.ForgetPWD(account).equals("1")){
                mailProviderService.SendMail(account);
                return JsonUtils.jsonPrint(1,"邮件发送成功!",null);
            }
            return JsonUtils.jsonPrint(0,"用户不存在!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,e.getMessage(),null);
        }
    }

    @RequestMapping(value="FileUpload")
    public String FileUpload(@RequestParam("file") MultipartFile[] file) {
        ArrayList data=new ArrayList();
        System.out.println(file.toString());
        if (file==null || file.length==0) {
            return JsonUtils.jsonPrint(1, null);
        }
        for(int i=0;i<file.length;i++){
            if(FileUploadUtils.IsImg(file[i]).equals("yes")){
                try{
                    String url=FileUploadUtils.Upload(file[i]);
                    if(!url.equals("error")){
                        data.add(url);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return JsonUtils.jsonPrint(1,null);
                }
            }
        }
        //System.out.print(JsonUtils.jsonPrint(0,data));
        return JsonUtils.jsonPrint(0,data);
    }

    @RequestMapping(value="SendMail",method= RequestMethod.POST)//注册
    public String SendMail(@RequestParam("email") String email){
        if(email==null || email.length()==0){
            return JsonUtils.jsonPrint(0,"没有收到用户邮箱!",null);
        }
        try{
            String result=userProviderService.ForgetPWD(email);
            if(result.equals("1")){
                mailProviderService.SendMail(email);
                return JsonUtils.jsonPrint(1,"邮件发送成功!",null);
            }
            return JsonUtils.jsonPrint(0,"未知错误!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(-1,"没有收到用户邮箱!",null);
        }
    }
}