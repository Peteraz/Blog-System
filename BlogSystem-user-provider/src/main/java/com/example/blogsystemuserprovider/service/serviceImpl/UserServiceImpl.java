package com.example.blogsystemuserprovider.service.serviceImpl;

import com.example.blogsystem.common.AgeUtils;
import com.example.blogsystem.common.FileUploadUtils;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.SHA256Utils;
import com.example.blogsystem.common.UUIDUtils;
import com.example.blogsystem.entity.User;
import com.example.blogsystemuserprovider.dao.UserMapper;
import com.example.blogsystemuserprovider.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final long ZERO = 0;

    @Override
    public String register(User user) {
        if (Objects.isNull(user) || StringUtils.isAnyBlank(user.getAccount(), user.getPassword(), user.getEmail())) {
            return JsonUtils.jsonPrint(-1, "缺少必要资料", null);
        }

        if (Objects.nonNull(userMapper.getUserByAccountAndPassword(user.getAccount(), null))) {
            return JsonUtils.jsonPrint(-2, "账号已存在", null);
        }

        if (Objects.nonNull(userMapper.getUserByEmail(user.getEmail()))) {
            return JsonUtils.jsonPrint(-3, "邮箱已使用", null);
        }

        user.setUserId(UUIDUtils.getUserId());
        user.setPassword(SHA256Utils.getSHA256(user.getPassword()));
        if (StringUtils.isNotBlank(user.getBirthday())) {
            String ageDetail = AgeUtils.getAgeDetail(user.getBirthday());
            logger.info(ageDetail);
            int yearIndex = ageDetail.indexOf("岁");
            if (yearIndex > 0) {
                user.setAge(Integer.valueOf(ageDetail.substring(0, yearIndex)));
            }
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
        user.setLoginTime(new Date());
        user.setLoginCount(user.getLoginCount() == null ? 1 : user.getLoginCount() + 1);
        userMapper.updateByUserSelective(user);
        //避免暴露密码
        user.setPassword(null);
        return JsonUtils.jsonPrint(1, "登录成功!", user);
    }

    @Override
    public String logout() {
        return JsonUtils.jsonPrint(1, "登出成功!", null);
    }

    @Override
    public String resetInfo(Map<String, String> map) {
        String userId = map.get("userId");
        if (StringUtils.isBlank(userId)) {
            return JsonUtils.jsonPrint(-1, "用户未登录", null);
        }

        User user = userMapper.getUserById(userId);
        if (user == null) {
            return JsonUtils.jsonPrint(-1, "用户不存在", null);
        }

        user.setBiography(map.get("biography"));
        user.setUserName(map.get("username"));
        user.setSex(map.get("sex"));
        if (StringUtils.isNotBlank(map.get("age"))) {
            user.setAge(Integer.valueOf(map.get("age")));
        }
        user.setBirthday(map.get("birthday"));
        user.setPhoneNumber(map.get("phoneNumber"));
        user.setEmail(map.get("email"));
        user.setState(map.get("state"));
        user.setCity(map.get("city"));
        userMapper.updateByUserSelective(user);
        user.setPassword(null);
        return JsonUtils.jsonPrint(1, "修改成功!", user);
    }

    @Override
    public String resetPWD(String userId, String password, String password1, String password2) {
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个新密码不一致", null);
        }
        try {
            if (StringUtils.isBlank(userId)) {
                return JsonUtils.jsonPrint(-3, "用户未登录", null);
            }
            User user = userMapper.getUserById(userId);
            if (user == null) {
                return JsonUtils.jsonPrint(-3, "用户不存在", null);
            }
            if (!SHA256Utils.getSHA256(password).equals(user.getPassword())) {
                return JsonUtils.jsonPrint(-2, "原密码错误", null);
            }
            user.setPassword(SHA256Utils.getSHA256(password1));
            userMapper.updateByUserSelective(user);
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            logger.error("reset password error: ", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    @Override
    public String resetPassword(String token, String password1, String password2) {
        if (!password1.equals(password2)) {
            return JsonUtils.jsonPrint(-1, "两个密码不一样", null);
        }
        try {
            if (StringUtils.isBlank(token)) {
                return JsonUtils.jsonPrint(-2, "非法访问", null);
            }
            // Token-scoped keys avoid one user's password reset request overwriting another user's request.
            String resetKey = "resetPwd:" + token;
            if (redisTemplate.getExpire(resetKey) == -2) {
                return JsonUtils.jsonPrint(-2, "修改密码时效已过期", null);
            }
            String email = String.valueOf(redisTemplate.opsForValue().get(resetKey));
            User user = userMapper.getUserByEmail(email);
            if (user == null) {
                return JsonUtils.jsonPrint(-3, "用户不存在", null);
            }
            user.setPassword(SHA256Utils.getSHA256(password1));
            userMapper.updateByUserSelective(user);
            redisTemplate.delete(resetKey);
            return JsonUtils.jsonPrint(1, "密码修改成功!", null);
        } catch (Exception e) {
            logger.error("reset password by token error: ", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

    @Override
    public String iconUpload(String userId, MultipartFile[] file) {
        if (StringUtils.isBlank(userId)) {
            return JsonUtils.jsonPrint(-2, "用户未登录", null);
        }
        for (MultipartFile multipartFile : file) {
            if ("yes".equals(FileUploadUtils.isImg(multipartFile))) {
                try {
                    String url = FileUploadUtils.upload(multipartFile);
                    if (!"error".equals(url)) {
                        User user = userMapper.getUserById(userId);
                        if (user == null) {
                            return JsonUtils.jsonPrint(-2, "用户不存在", null);
                        }
                        user.setUserIcon(url);
                        userMapper.updateByUserSelective(user);
                        user.setPassword(null);
                        return JsonUtils.jsonPrint(1, "头像上传成功!", user);
                    }
                    return JsonUtils.jsonPrint(-1, "头像上传失败!", null);
                } catch (Exception e) {
                    logger.error("upload avatar error: ", e);
                    return JsonUtils.jsonPrint(0, e.getMessage(), null);
                }
            }
        }
        return JsonUtils.jsonPrint(0, "发生错误!", null);
    }

    @Override
    public String forgetPWD(String email) {
        User user = userMapper.getUserByEmail(email);
        if (user != null) {
            return JsonUtils.jsonPrint(1, "邮件发送成功", null);
        }
        return JsonUtils.jsonPrint(-1, "邮件发送失败", null);
    }
}
