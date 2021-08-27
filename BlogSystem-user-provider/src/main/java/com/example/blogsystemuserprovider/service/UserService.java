package com.example.blogsystemuserprovider.service;

import com.example.blogsystem.entity.User;

public interface UserService {
    User getUserById(String userid);

    User getUserByAccountAndPassword(String account,String password);

    int deleteById(String userid);

    int insert(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateById(User record);
}
