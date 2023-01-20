package com.example.blogsystemconsumer.resilience4j;

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
    public String Register(@RequestBody Map<String, String> map) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String Logout() {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String ResetInfo(@RequestBody Map<String, String> map) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String IconUpload(@RequestPart("file") MultipartFile[] file) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String ResetPWD(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String ResetPassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        return "连接超时,请稍后重试!";
    }

    @Override
    public String ForgetPWD(@RequestParam("account") String account) {
        return "连接超时,请稍后重试!";
    }
}
