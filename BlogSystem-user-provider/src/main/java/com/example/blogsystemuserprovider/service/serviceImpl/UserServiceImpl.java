package com.example.blogsystemuserprovider.service.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.blogsystem.common.*;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.dao.UserMapper;
import com.example.blogsystemuserprovider.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public String register(Map<String, String> map) {
        User user = new User();
        long count = 0;
        String age = "";
        try {
            if (userMapper.getUserByAccountAndPassword(map.get("account"), null) != null) {
                return JsonUtils.jsonPrint(-1, "账号已存在!", null); //账号已存在
            } else if (userMapper.getUserByEmail(map.get("email")) != null) {
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
                userMapper.insertSelective(user);
                return JsonUtils.jsonPrint(1, "注册成功!", null); //注册成功
            } else {
                return JsonUtils.jsonPrint(-3, "注册失败!", null); //注册失败
            }
        } catch (Exception e) {
            logger.error("register was an error", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null); //注册失败
        }
    }

    @Override
    public String login(String account, String password) {
        User user;
        try {
            user = userMapper.getUserByAccountAndPassword(account, null);
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
                userMapper.updateByUserSelective(user);
                user.setPassword("null");   //避免暴露密码
                redisTemplate.opsForValue().set("user", JSON.toJSONString(user), 7, TimeUnit.DAYS); //redis缓存
                return JsonUtils.jsonPrint(1, "登录成功!", null); //登录成功
            }
        } catch (Exception e) {
            logger.error("login was an error", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);  //登录失败
        }
    }

    @Override
    public String logout() {
        //清空用户资料
        try {
            redisTemplate.delete("user");
            return JsonUtils.jsonPrint(1, "登出成功!", null); //登出成功
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null); //登出出错
        }
    }

    @Override
    public String resetInfo(@RequestBody Map<String, String> map) {
        User user = new User();
        try {
            user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
            if (user == null) {
                return JsonUtils.jsonPrint(-1, "用户不存在!", null);  //用户不存在
            }
            user = userMapper.getUserById(user.getUserId());
            user.setBiography(map.get("biography"));
            user.setUserName(map.get("username"));
            user.setSex(map.get("sex"));
            user.setAge(Integer.valueOf(map.get("age")));
            user.setBirthday(map.get("birthday"));
            user.setPhoneNumber(map.get("phoneNumber"));
            user.setEmail(map.get("email"));
            user.setState(map.get("state"));
            user.setCity(map.get("city"));
            userMapper.updateByUserSelective(user);
            user.setPassword("null");
            //刷新redis缓存
            redisTemplate.opsForValue().getAndSet("user", JSON.toJSONString(user));
            return JsonUtils.jsonPrint(1, "修改成功!", null); //修改成功
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);  //修改发生错误
        }
    }

    @Override
    public String resetPWD(String password, String password1, String password2) {
        User user = new User();
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个新密码不一致!", null); //两个密码不一致
        }
        try {
            if (StringUtils.isNotBlank(String.valueOf(redisTemplate.opsForValue().get("user")))) {
                user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                user = userMapper.getUserById(user.getUserId());
            }
            if (!SHA256Utils.getSHA256(password).equals(user.getPassword())) {
                return JsonUtils.jsonPrint(-2, "原密码错误!", null); //原密码不对
            }
            user.setPassword(SHA256Utils.getSHA256(password1));
            userMapper.updateByUserSelective(user);
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    public String resetPassword(String password1, String password2) {
        User user;
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个密码不一样!", null);     //两个密码不一样
        }
        try {
            if (redisTemplate.getExpire("resetEmail") != 0 && redisTemplate.getExpire("resetEmail") == -2) {
                return JsonUtils.jsonPrint(-2, "修改密码过期了!", null); //修改密码的时候已过期
            }
            String email = String.valueOf(redisTemplate.opsForValue().get("resetEmail"));
            user = userMapper.getUserByEmail(email);
            user.setPassword(SHA256Utils.getSHA256(password1));
            userMapper.updateByUserSelective(user);
            redisTemplate.delete("resetPwdToken");
            redisTemplate.delete("resetEmail");
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    @Override
    public String iconUpload(@RequestPart("file") MultipartFile[] file) {
        User user;
        for (int i = 0; i < file.length; i++) {
            if (FileUploadUtils.isImg(file[i]).equals("yes")) {
                try {
                    String url = FileUploadUtils.upload(file[i]);
                    if (!url.equals("error")) {
                        user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                        user = userMapper.getUserById(user.getUserId());
                        user.setUserIcon(url);
                        userMapper.updateByUserSelective(user);
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

    @Override
    public String forgetPWD(@RequestParam("email") String email) {
        User user;
        try {
            user = userMapper.getUserByEmail(email);
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
