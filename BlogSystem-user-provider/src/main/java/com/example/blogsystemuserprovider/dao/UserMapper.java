package com.example.blogsystemuserprovider.dao;

import com.example.blogsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(String userId);

    User getUserByAccountAndPassword(String account,String password);

    User getUserByEmail(String email);

    int deleteById(String userId);

    int insert(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateById(User record);
}