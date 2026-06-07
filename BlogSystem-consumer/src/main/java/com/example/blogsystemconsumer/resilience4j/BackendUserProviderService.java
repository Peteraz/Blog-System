package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystem.entity.User;
import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystemconsumer.service.UserProviderService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class BackendUserProviderService implements UserProviderService {
    @Override
    public String Register(@RequestBody User user) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String Logout() {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String ResetInfo(@RequestBody Map<String, String> map) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String IconUpload(@RequestParam("userId") String userId, @RequestPart("file") MultipartFile[] file) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String ResetPWD(@RequestParam("userId") String userId, @RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String ResetPassword(@RequestParam("token") String token, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }

    @Override
    public String ForgetPWD(@RequestParam("email") String email) {
        return JsonUtils.jsonPrint(0, "连接超时,请稍后重试!", null);
    }
}
