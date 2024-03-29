package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.UserProviderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ConsumerController {
    //@Resource默认按byName自动注入,有两个重要属性分别是name和type

    @Resource
    private UserProviderService userProviderService;

    @Resource
    private ArticleProviderService articleProviderService;

    @Resource
    private MailProviderService mailProviderService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    /**
     * 注册
     */
    @RequestMapping(value = "Register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {
        return userProviderService.Register(user);  //返回调用结果
    }

    /**
     * 登录
     */
    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String login(@RequestParam("account") String account, @RequestParam("password") String password) {  //登录
        if (StringUtils.isBlank(account)) {
            return JsonUtils.jsonPrint(-4, "请输入账号!", null);
        } else if (StringUtils.isBlank(password)) {
            return JsonUtils.jsonPrint(-5, "请输入密码!", null);
        }
        return userProviderService.Login(account, password);  //返回调用结果
    }

    /**
     * 发表文章
     */
    @RequestMapping(value = "createArticle")
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category, @RequestParam("articleContents") String articleContents) {
        if (StringUtils.isEmpty(articleName) || StringUtils.isEmpty(category) || StringUtils.isEmpty(articleContents)) {
            return JsonUtils.jsonPrint(-1, "没收到数据!", null);
        }
        return articleProviderService.createArticle(articleName, category, articleContents);
    }

    /**
     * 登出
     */
    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    public String logout() {  //登出
        return userProviderService.Logout();  //返回调用结果
    }

    /**
     * 修改资料
     */
    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String resetInfo(@RequestBody Map<String, String> map) {
        if (map.isEmpty()) {
            return JsonUtils.jsonPrint(-3, "接收不到数据!", null);
        }
        return userProviderService.ResetInfo(map); //返回调用结果
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String resetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password1) || StringUtils.isEmpty(password2)) {
            return JsonUtils.jsonPrint(-3, "有密码为空!", null);
        }
        return userProviderService.ResetPWD(password, password1, password2);

    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        if (StringUtils.isEmpty(password1) || StringUtils.isEmpty(password2)) {
            return JsonUtils.jsonPrint(-3, "密码不能空!", null);
        }
        return userProviderService.ResetPassword(password1, password2);
    }

    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    public String forgetPWD(@RequestParam("account") String account) {
        try {
            if (userProviderService.ForgetPWD(account).equals("1")) {
                return mailProviderService.sendMail(account);
            }
            return JsonUtils.jsonPrint(-1, "用户不存在!", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ForgetPWD wan an error :", e);
        }
        return JsonUtils.jsonPrint(0, "发生错误!", null);
    }

    @RequestMapping(value = "FileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile[] file) {
        List<String> data = new ArrayList<>();
        if (file == null || file.length == 0) {
            return JsonUtils.jsonPrint(1, null);
        }
        for (MultipartFile multipartFile : file) {
            if (FileUploadUtils.isImg(multipartFile).equals("yes")) {
                try {
                    String url = FileUploadUtils.upload(multipartFile);
                    if (!url.equals("error")) {
                        data.add(url);
                    }
                } catch (Exception e) {
                    logger.error("upload file error: ", e);
                    return JsonUtils.jsonPrint(1, null);
                }
            }
        }
        return JsonUtils.jsonPrint(0, data);
    }

    @RequestMapping(value = "IconUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String iconUpload(@RequestPart("file") MultipartFile[] file) {
        if (file == null || file.length == 0) {
            return JsonUtils.jsonPrint(-2, "没有选择照片!", null);
        }
        return userProviderService.IconUpload(file);
    }

    @RequestMapping(value = "SendMail", method = RequestMethod.POST)//注册
    public String sendMail(@RequestParam("email") String email) {
        if (StringUtils.isNotBlank(email)) {
            return JsonUtils.jsonPrint(-4, "没有收到用户邮箱!", null);
        }
        return userProviderService.ForgetPWD(email);
    }

}