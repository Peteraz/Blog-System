package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.ProductService;
import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

    @RequestMapping(value="getConsumer")
    public String getConsumer(){
        String str=productService.getService();
        return str;
    }

    @RequestMapping(value="Register",method = RequestMethod.POST)
    public String Register(@RequestBody Map<String,String> map){
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
        if(account.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入账号!",null);
        }else if(password.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入密码!",null);
        }
        try{
            String result=userProviderService.Login(account,password);
            System.out.println(result);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"登录成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"登录账号错误!",null);
            }else if(result.equals("-2")){
                return JsonUtils.jsonPrint(-1,"登录密码错误!",null);
            }
            return JsonUtils.jsonPrint(-3,"未知错误!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public String Logout(HttpSession session){
        try{
            String result=userProviderService.Logout(session);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"登出成功!",null);
            }
            return JsonUtils.jsonPrint(-1,"登出发生错了!",null);
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
        System.out.print(JsonUtils.jsonPrint(0,data));
        return JsonUtils.jsonPrint(0,data);
    }
}