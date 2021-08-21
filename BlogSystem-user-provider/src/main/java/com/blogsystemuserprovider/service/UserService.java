package com.blogsystemuserprovider.service;

import com.blogsystem.entity.User;

public interface UserService {
    User getUserById(String userid);

    User getUserByAccountAndPassword(String account,String password);

    int deleteById(String userid);

    int insertUser(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateByUser(User record);
}
