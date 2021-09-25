package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.ProductService;
import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    private static final String BACKEND="backendA";


    @RequestMapping(value="getConsumer")
    @CircuitBreaker(name = BACKEND, fallbackMethod = "Consumerfallback")
    public String getConsumer(){
        String str=productService.getService();
        return str;
    }

    public String Consumerfallback(Throwable e){
        return "连接超时,请稍后重试!";
    }

    @RequestMapping(value="Register",method = RequestMethod.POST)
    @CircuitBreaker(name = BACKEND, fallbackMethod = "Registerfallback")
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

    public String Registerfallback(){
        return "连接超时,请稍后重试!";
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    @CircuitBreaker(name = BACKEND, fallbackMethod = "Loginfallback")
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password){
        if(account.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入账号!",null);
        }else if(password.isEmpty()){
            return JsonUtils.jsonPrint(-1,"请输入密码!",null);
        }
        try{
            String result=userProviderService.Login(account,password);
            System.out.println(result);
            switch (result) {
                case "1":
                    return JsonUtils.jsonPrint(1, "登录成功!", null);
                case "-1":
                    return JsonUtils.jsonPrint(-1, "登录账号错误!", null);
                case "-2":
                    return JsonUtils.jsonPrint(-1, "登录密码错误!", null);
            }
            return JsonUtils.jsonPrint(-3,result,null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    public String Loginfallback(){
        return "连接超时,请稍后重试!";
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    @CircuitBreaker(name = BACKEND, fallbackMethod = "Logoutfallback")
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

    public String Logoutfallback(){
        return "连接超时,请稍后重试!";
    }

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)
    @CircuitBreaker(name = BACKEND, fallbackMethod = "ForgetPWDfallback")
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

    public String ForgetPWDfallback(){
        return "连接超时,请稍后重试!";
    }

    @RequestMapping(value="FileUpload")
    @CircuitBreaker(name = BACKEND, fallbackMethod = "FileUploadfallback")
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

    public String FileUploadfallback(){
        return "连接超时,请稍后重试!";
    }
}