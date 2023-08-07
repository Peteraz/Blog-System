package com.example.blogsystem.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private String userId;

    private String account;

    private String password;

    private String email;

    private String userName;

    private String userIcon;

    private Integer age;

    private String sex;

    private String phoneNumber;

    private String birthday;

    private String state;

    private String city;

    private String biography;

    private Date createTime;

    private Date loginTime;

    private Date lastLoginTime;

    private Long loginCount;

    private String identity;

}