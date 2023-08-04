package com.example.blogsystemuserprovider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.blogsystem.common.*;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "Register", method = RequestMethod.POST)
    public String Register(@RequestBody Map<String, String> map) {
        User user = new User();
        long count = 0;
        String age = "";
        try {
            if (userService.getUserByAccountAndPassword(map.get("account"), null) != null) {
                return JsonUtils.jsonPrint(-1, "账号已存在!", null); //账号已存在
            } else if (userService.getUserByEmail(map.get("email")) != null) {
                return JsonUtils.jsonPrint(-2, "邮箱已使用!", null); //邮箱已使用
            } else if (!map.isEmpty()) {
                user.setUserId(UUIDUtils.getUserId());
                user.setAccount(map.get("account"));
                user.setPassword(SHA256Utils.getSHA256(map.get("password")));
                user.setUserName(map.get("name"));
                user.setEmail(map.get("email"));
                user.setBirthday(map.get("birthday"));
                age = AgeUtils.getAgeDetail(map.get("birthday"));
                logger.info(age);
                age = age.substring(0, age.indexOf("岁"));
                user.setAge(Integer.valueOf(age));
                user.setSex(map.get("sex"));
                user.setPhoneNumber(map.get("phone_number"));
                user.setState(map.get("state"));
                user.setCity(map.get("city"));
                user.setCreateTime(new Date());
                user.setLoginCount(count);
                userService.insertSelective(user);
                return JsonUtils.jsonPrint(1, "注册成功!", null); //注册成功
            } else {
                return JsonUtils.jsonPrint(-3, "注册失败!", null); //注册失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null); //注册失败
        }
    }

    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password) {
        User user = new User();
        try {
            user = userService.getUserByAccountAndPassword(account, null);
            if (user == null) {
                return JsonUtils.jsonPrint(-1, "登录账号错误!", null); //登录账号错误
            } else if (!user.getPassword().equals(SHA256Utils.getSHA256(password))) {
                return JsonUtils.jsonPrint(-2, "登录密码错误!", null); //登录密码错误
            } else {
                if (user.getLoginTime() != null) {
                    user.setLastLoginTime(user.getLoginTime());
                }
                user.setLoginTime(new Date());  //当前登录时间
                user.setLoginCount(user.getLoginCount() + 1);  //登录次数+1
                userService.updateByUserSelective(user);
                user.setPassword("null");   //避免暴露密码
                redisTemplate.opsForValue().set("user", JSON.toJSONString(user), 7, TimeUnit.DAYS); //redis缓存
                return JsonUtils.jsonPrint(1, "登录成功!", null); //登录成功
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);  //登录失败
        }
    }

    @RequestMapping(value = "Logout", method = RequestMethod.POST)
    public String Logout() {
        //清空用户资料
        try {
            redisTemplate.delete("user");
            return JsonUtils.jsonPrint(1, "登出成功!", null); //登出成功
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null); //登出出错
        }
    }

    @RequestMapping(value = "ResetInfo", method = RequestMethod.POST)
    public String ResetInfo(@RequestBody Map<String, String> map) {
        User user = new User();
        try {
            user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            if (user == null) {
                return JsonUtils.jsonPrint(-1, "用户不存在!", null);  //用户不存在
            }
            user = userService.getUserById(user.getUserId());
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
            return JsonUtils.jsonPrint(1, "修改成功!", null); //修改成功
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);  //修改发生错误
        }
    }

    @RequestMapping(value = "ResetPWD", method = RequestMethod.POST)
    public String ResetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        User user = new User();
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个新密码不一致!", null); //两个密码不一致
        }
        try {
            if (StringUtils.isNotBlank(String.valueOf(redisTemplate.opsForValue().get("user")))) {
                user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                user = userService.getUserById(user.getUserId());
            }
            if (!SHA256Utils.getSHA256(password).equals(user.getPassword())) {
                return JsonUtils.jsonPrint(-2, "原密码错误!", null); //原密码不对
            }
            user.setPassword(SHA256Utils.getSHA256(password1));
            userService.updateByUserSelective(user);
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    @RequestMapping(value = "ResetPassword", method = RequestMethod.POST)
    public String ResetPassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        User user = null;
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个密码不一样!", null);     //两个密码不一样
        }
        try {
            if (redisTemplate.getExpire("resetEmail") != 0 && redisTemplate.getExpire("resetEmail") == -2) {
                return JsonUtils.jsonPrint(-2, "修改密码过期了!", null); //修改密码的时候已过期
            }
            String email = String.valueOf(redisTemplate.opsForValue().get("resetEmail"));
            user = userService.getUserByEmail(email);
            user.setPassword(SHA256Utils.getSHA256(password1));
            userService.updateByUserSelective(user);
            redisTemplate.delete("resetPwdToken");
            redisTemplate.delete("resetEmail");
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    @RequestMapping(value = "IconUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String IconUpload(@RequestPart("file") MultipartFile[] file) {
        User user = null;
        for (int i = 0; i < file.length; i++) {
            if (FileUploadUtils.IsImg(file[i]).equals("yes")) {
                try {
                    String url = FileUploadUtils.Upload(file[i]);
                    if (!url.equals("error")) {
                        user = JSONArray.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                        user = userService.getUserById(user.getUserId());
                        user.setUserIcon(url);
                        userService.updateByUserSelective(user);
                        return JsonUtils.jsonPrint(1, "头像上传成功!", null);
                    }
                    return JsonUtils.jsonPrint(-1, "头像上传失败!", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonUtils.jsonPrint(0, e.getMessage(), null);
                }
            }
        }
        return JsonUtils.jsonPrint(0, "发生错误!", null);
    }

    @RequestMapping(value = "ForgetPWD", method = RequestMethod.POST)
    public String ForgetPWD(@RequestParam("email") String email) {
        User user = new User();
        try {
            user = userService.getUserByEmail(email);
            if (user != null) {
                return JsonUtils.jsonPrint(1, "邮件发送成功!", null);
            }
            return JsonUtils.jsonPrint(-1, "邮件发送失败!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }
}