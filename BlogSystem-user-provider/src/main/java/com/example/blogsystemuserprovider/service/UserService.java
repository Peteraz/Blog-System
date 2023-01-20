package com.example.blogsystemuserprovider.service;

import com.example.blogsystem.entity.User;

public interface UserService {
    User getUserById(String userId);

    User getUserByAccountAndPassword(String account,String password);

    User getUserByEmail(String email);

    int deleteById(String userId);

    int insert(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateById(User record);
}
