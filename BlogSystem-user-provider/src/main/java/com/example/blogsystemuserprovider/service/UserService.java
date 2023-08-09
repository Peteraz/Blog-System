package com.example.blogsystemuserprovider.service;

import com.example.blogsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {

    String register(User user);

    String login(String account, String password);

    String logout();

    String resetInfo(Map<String, String> map);

    String resetPWD(String password, String password1, String password2);

    String resetPassword(String password1, String password2);

    String iconUpload(MultipartFile[] file);

    String forgetPWD(String email);

}
