package com.example.blogsystemuserprovider.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {

    String register(Map<String, String> map);

    String login(String account, String password);

    String logout();

    String resetInfo(Map<String, String> map);

    String resetPWD(String password, String password1, String password2);

    String resetPassword(String password1, String password2);

    String iconUpload(MultipartFile[] file);

    String forgetPWD(String email);

}
