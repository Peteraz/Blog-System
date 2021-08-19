package com.example.blogsystemuserprovider.service;

import com.example.blogsystem.entity.User;

public interface UserService {
    User getUserById(String userid);

    User getUserByAccount(String account);

    int deleteById(String userid);

    int insertUser(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateByUser(User record);
}
