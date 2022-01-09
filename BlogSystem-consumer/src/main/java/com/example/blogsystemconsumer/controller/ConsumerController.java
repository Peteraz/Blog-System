package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.ProductService;
import com.example.blogsystemconsumer.service.UserProviderService;
import com.example.blogsystem.common.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
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
    private ArticleProviderService articleProviderService;

    @Resource
    private MailProviderService mailProviderService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value="getConsumer")
    public String getConsumer(){
        String result=productService.getService();
        return result;
    }

    @RequestMapping(value="Register",method = RequestMethod.POST)
    public String Register(@RequestBody Map<String, String> map){  //注册
        if(map.isEmpty()) {
            return JsonUtils.jsonPrint(-3,"收不到注册信息!",null);
        }
        return userProviderService.Register(map);  //返回调用结果
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password){  //登录
        if(StringUtils.isBlank(account)){
            return JsonUtils.jsonPrint(-4,"请输入账号!",null);
        }else if(StringUtils.isBlank(password)){
            return JsonUtils.jsonPrint(-5,"请输入密码!",null);
        }
        return userProviderService.Login(account,password);  //返回调用结果
    }

    @RequestMapping(value="createArticle")
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category,@RequestParam("articleContents") String articleContents){
        if(articleName.isEmpty() || articleName == null || category.isEmpty() || category == null || articleContents.isEmpty() || articleContents == null) {
            return JsonUtils.jsonPrint(-1,"没收到数据!",null);
        }
        return articleProviderService.createArticle(articleName,category,articleContents);
    }

    @RequestMapping(value="Logout",method= RequestMethod.POST)
    public String Logout(){  //登出
        return userProviderService.Logout();  //返回调用结果
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String ResetInfo(@RequestBody Map<String, String> map){
        if(map.isEmpty()){
            return JsonUtils.jsonPrint(-3,"接收不到数据!",null);
        }
        return userProviderService.ResetInfo(map); //返回调用结果
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String ResetPWD(@RequestParam("password") String password,@RequestParam("password1") String password1,@RequestParam("password2") String password2){
        User user=new User();
        if(password.isEmpty() || password == null || password1.isEmpty() || password1 == null || password2.isEmpty() || password2 == null){
            return JsonUtils.jsonPrint(-4,"有密码为空!",null);
        }
        try{
            String result=userProviderService.ResetPWD(password,password1,password2);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"密码修改成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"新密码不一致!",null);
            }else if(result.equals("-2")){
                return JsonUtils.jsonPrint(-2,"原密码错误!",null);
            }
            return JsonUtils.jsonPrint(-3,"密码修改失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String ResetPassword(@RequestParam("password1") String password1,@RequestParam("password2") String password2) {
        if(password1.isEmpty() || password1==null || password2.isEmpty() || password2==null){
            return JsonUtils.jsonPrint(-3,"密码不能空!",null);
        }
        try{
            String result=userProviderService.ResetPassword(password1,password2);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1,"密码修改成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1,"密码不一样!",null);
            }else if(result.equals("-2")){
                return JsonUtils.jsonPrint(-2,"修改密码过期了!",null);
            }
            return JsonUtils.jsonPrint(-3,"密码修改失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value="ForgetPWD",method= RequestMethod.POST)
    public String ForgetPWD(@RequestParam("account") String account){
        try{
            if(userProviderService.ForgetPWD(account).equals("1")){
                mailProviderService.SendMail(account);
                return JsonUtils.jsonPrint(1,"邮件发送成功!",null);
            }
            return JsonUtils.jsonPrint(-1,"用户不存在!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }

    @RequestMapping(value="FileUpload")
    public String FileUpload(@RequestParam("file") MultipartFile[] file) {
        ArrayList data=new ArrayList();
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
        return JsonUtils.jsonPrint(0,data);
    }

    @RequestMapping(value="IconUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String IconUpload(@RequestPart("file") MultipartFile[] file) {
        if (file==null || file.length==0) {
            return JsonUtils.jsonPrint(-2, "没有选择照片!",null);
        }
        try{
            String result=userProviderService.IconUpload(file);
            if(result.equals("1")){
                return JsonUtils.jsonPrint(1, "头像上传成功!",null);
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-1, "头像上传失败!",null);
            }
            return JsonUtils.jsonPrint(-3, "头像上传失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(),null);
        }
    }

    @RequestMapping(value="SendMail",method= RequestMethod.POST)//注册
    public String SendMail(@RequestParam("email") String email){
        if(email==null || email.length()==0){
            return JsonUtils.jsonPrint(-4,"没有收到用户邮箱!",null);
        }
        try{
            String result=userProviderService.ForgetPWD(email);
            if(result.equals("1")){
                String mailResult=mailProviderService.SendMail(email);
                if(mailResult.equals("1")){
                    return JsonUtils.jsonPrint(1,"邮件发送成功!",null);
                }else{
                    return JsonUtils.jsonPrint(-1,"邮件发送失败!",null);
                }
            }else if(result.equals("-1")){
                return JsonUtils.jsonPrint(-2,"用户不存在!",null);
            }
            return JsonUtils.jsonPrint(-3,"邮件发送失败!",null);
        }catch(Exception e){
            e.printStackTrace();
            return JsonUtils.jsonPrint(0,e.getMessage(),null);
        }
    }
}