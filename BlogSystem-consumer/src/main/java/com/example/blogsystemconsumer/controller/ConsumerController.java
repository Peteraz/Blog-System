package com.example.blogsystemconsumer.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import com.example.blogsystemconsumer.service.MailProviderService;
import com.example.blogsystemconsumer.service.UserProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ConsumerController {
    // Keep the logged-in user scoped to the browser session instead of a shared Redis key.
    private static final String SESSION_USER = "user";

    @Resource
    private UserProviderService userProviderService;

    @Resource
    private ArticleProviderService articleProviderService;

    @Resource
    private MailProviderService mailProviderService;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @RequestMapping(value = "Register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {
        return userProviderService.Register(user);
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String login(@RequestParam("account") String account, @RequestParam("password") String password, HttpSession session) {
        if (StringUtils.isBlank(account)) {
            return JsonUtils.jsonPrint(-4, "请输入账号", null);
        } else if (StringUtils.isBlank(password)) {
            return JsonUtils.jsonPrint(-5, "请输入密码", null);
        }

        String response = userProviderService.Login(account, password);
        try {
            // The user provider returns sanitized user data on login; cache it in this session for later requests.
            JSONObject json = JSONObject.parseObject(response);
            if (isSuccess(json)) {
                User user = json.getObject("data", User.class);
                if (user != null) {
                    session.setAttribute(SESSION_USER, user);
                }
            }
        } catch (Exception e) {
            logger.warn("parse login response error: {}", response, e);
        }
        return response;
    }

    @RequestMapping(value = "createArticle")
    public String createArticle(@RequestParam("articleName") String articleName, @RequestParam("category") String category,
                                @RequestParam("articleContents") String articleContents, HttpSession session) {
        User user = getSessionUser(session);
        if (user == null) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        if (StringUtils.isAnyEmpty(articleName, category, articleContents)) {
            return JsonUtils.jsonPrint(-1, "没收到数据", null);
        }
        return articleProviderService.createArticle(user.getUserId(), articleName, category, articleContents);
    }

    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        String response = userProviderService.Logout();
        session.invalidate();
        return response;
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String resetInfo(@RequestBody Map<String, String> map, HttpSession session) {
        User user = getSessionUser(session);
        if (user == null) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        if (map.isEmpty()) {
            return JsonUtils.jsonPrint(-3, "接收不到数据!", null);
        }
        // Provider services are stateless here, so the consumer passes the current user id explicitly.
        map.put("userId", user.getUserId());
        String response = userProviderService.ResetInfo(map);
        refreshSessionUser(response, session);
        return response;
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String resetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1,
                           @RequestParam("password2") String password2, HttpSession session) {
        User user = getSessionUser(session);
        if (user == null) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        if (StringUtils.isAnyEmpty(password, password1, password2)) {
            return JsonUtils.jsonPrint(-3, "有密码为空", null);
        }
        return userProviderService.ResetPWD(user.getUserId(), password, password1, password2);
    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password1") String password1,
                                @RequestParam("password2") String password2) {
        if (StringUtils.isBlank(token)) {
            return JsonUtils.jsonPrint(-2, "非法访问", null);
        }
        if (StringUtils.isAnyEmpty(password1, password2)) {
            return JsonUtils.jsonPrint(-3, "密码不能为空", null);
        }
        return userProviderService.ResetPassword(token, password1, password2);
    }

    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    public String forgetPWD(@RequestParam("email") String email) {
        return userProviderService.ForgetPWD(email);
    }

    @RequestMapping(value = "FileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile[] file) {
        List<String> data = new ArrayList<>();
        if (file == null || file.length == 0) {
            return JsonUtils.jsonPrint(1, null);
        }
        for (MultipartFile multipartFile : file) {
            if ("yes".equals(FileUploadUtils.isImg(multipartFile))) {
                try {
                    String url = FileUploadUtils.upload(multipartFile);
                    if (!"error".equals(url)) {
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
    public String iconUpload(@RequestPart("file") MultipartFile[] file, HttpSession session) {
        User user = getSessionUser(session);
        if (user == null) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        if (file == null || file.length == 0) {
            return JsonUtils.jsonPrint(-2, "没有选择照片!", null);
        }
        String response = userProviderService.IconUpload(user.getUserId(), file);
        refreshSessionUser(response, session);
        return response;
    }

    @RequestMapping(value = "SendMail", method = RequestMethod.POST)
    public String sendMail(@RequestParam("email") String email) {
        if (StringUtils.isBlank(email)) {
            return JsonUtils.jsonPrint(-4, "没有收到用户邮箱!", null);
        }
        String userCheck = userProviderService.ForgetPWD(email);
        try {
            JSONObject json = JSONObject.parseObject(userCheck);
            if (isSuccess(json)) {
                return mailProviderService.sendMail(email);
            }
        } catch (Exception e) {
            logger.error("check reset password email error: ", e);
            return JsonUtils.jsonPrint(0, "发生错误!", null);
        }
        return JsonUtils.jsonPrint(-1, "用户不存在", null);
    }

    private User getSessionUser(HttpSession session) {
        Object user = session.getAttribute(SESSION_USER);
        return user instanceof User ? (User) user : null;
    }

    private void refreshSessionUser(String response, HttpSession session) {
        try {
            // Profile/avatar updates return fresh user data; keep the session copy in sync.
            JSONObject json = JSONObject.parseObject(response);
            if (isSuccess(json)) {
                User user = json.getObject("data", User.class);
                if (user != null) {
                    session.setAttribute(SESSION_USER, user);
                }
            }
        } catch (Exception e) {
            logger.warn("parse user response error: {}", response, e);
        }
    }

    private boolean isSuccess(JSONObject json) {
        return json != null && json.getInteger("status") != null && json.getInteger("status") == 1;
    }
}
