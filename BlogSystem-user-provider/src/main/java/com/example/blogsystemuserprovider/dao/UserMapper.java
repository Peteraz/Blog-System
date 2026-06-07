package com.example.blogsystemuserprovider.dao;

import com.example.blogsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User getUserById(String userId);

    User getUserByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    User getUserByEmail(String email);

    int deleteById(String userId);

    int insert(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateById(User record);
}
