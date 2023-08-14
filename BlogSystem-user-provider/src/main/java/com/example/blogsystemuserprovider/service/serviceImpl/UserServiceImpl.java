package com.example.blogsystemuserprovider.service.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.blogsystem.common.*;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.dao.UserMapper;
import com.example.blogsystemuserprovider.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final long ZERO = 0;

    @Override
    public String register(User user) {
        if (Objects.isNull(user) || StringUtils.isAnyBlank(user.getAccount(), user.getPassword(), user.getEmail())) {
            return JsonUtils.jsonPrint(-1, "缺少必须的资料!", null);
        }

        if (Objects.nonNull(userMapper.getUserByAccountAndPassword(user.getAccount(), null))) {
            return JsonUtils.jsonPrint(-2, "账号已存在!", null);
        }

        if (Objects.nonNull(userMapper.getUserByEmail(user.getEmail()))) {
            return JsonUtils.jsonPrint(-3, "邮箱已使用!", null);
        }

        user.setUserId(UUIDUtils.getUserId());
        user.setPassword(SHA256Utils.getSHA256(user.getPassword()));
        String age;
        if (StringUtils.isNotBlank(user.getBirthday())) {
            age = AgeUtils.getAgeDetail(user.getBirthday());
            logger.info(age);
            age = age.substring(0, age.indexOf("岁"));
            user.setAge(Integer.valueOf(age));
        }
        user.setCreateTime(new Date());
        user.setLoginCount(ZERO);
        userMapper.insertSelective(user);
        return JsonUtils.jsonPrint(1, "注册成功!", null);
    }

    @Override
    public String login(String account, String password) {
        User user = userMapper.getUserByAccountAndPassword(account, null);
        if (Objects.isNull(user)) {
            return JsonUtils.jsonPrint(-1, "登录账号错误!", null);
        }

        if (!user.getPassword().equals(SHA256Utils.getSHA256(password))) {
            return JsonUtils.jsonPrint(-2, "登录密码错误!", null);
        }

        if (user.getLoginTime() != null) {
            user.setLastLoginTime(user.getLoginTime());
        }
        user.setLoginTime(new Date());  //当前登录时间
        user.setLoginCount(user.getLoginCount() + 1);  //登录次数+1
        userMapper.updateByUserSelective(user);
        //避免暴露密码
        user.setPassword(null);
        //redis缓存,保存7天
        redisTemplate.opsForValue().set("user", JSON.toJSONString(user), 7, TimeUnit.DAYS);
        return JsonUtils.jsonPrint(1, "登录成功!", null);
    }

    @Override
    public String logout() {
        redisTemplate.delete("user");
        return JsonUtils.jsonPrint(1, "登出成功!", null);
    }

    @Override
    public String resetInfo(@RequestBody Map<String, String> map) {
        User user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
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
    }

    @Override
    public String resetPWD(String password, String password1, String password2) {
        User user = new User();
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个新密码不一致!", null);
        }
        try {
            if (StringUtils.isNotBlank(String.valueOf(redisTemplate.opsForValue().get("user")))) {
                user = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get("user")), User.class);
                user = userMapper.getUserById(user.getUserId());
            }
            if (!SHA256Utils.getSHA256(password).equals(user.getPassword())) {
                return JsonUtils.jsonPrint(-2, "原密码错误!", null);
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
            return JsonUtils.jsonPrint(-1, "两个密码不一样!", null);
        }
        try {
            if (redisTemplate.getExpire("resetEmail") != 0 && redisTemplate.getExpire("resetEmail") == -2) {
                return JsonUtils.jsonPrint(-2, "修改密码时效已过了!", null);
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
        for (MultipartFile multipartFile : file) {
            if (FileUploadUtils.isImg(multipartFile).equals("yes")) {
                try {
                    String url = FileUploadUtils.upload(multipartFile);
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
        User user = userMapper.getUserByEmail(email);
        if (user != null) {
            return JsonUtils.jsonPrint(1, "邮件发送成功!", null);
        }
        return JsonUtils.jsonPrint(-1, "邮件发送失败!", null);

    }
}
