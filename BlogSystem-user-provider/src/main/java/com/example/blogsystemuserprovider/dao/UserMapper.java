package com.example.blogsystemuserprovider.dao;

import com.example.blogsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

//每个dao层的接口(interface)要添加加@Mapper, 在编译之后会生成相应的接口实现类
@Mapper
public interface UserMapper {
    User getUserById(String userid);

    User getUserByAccountAndPassword(String account,String password);

    int deleteById(String userid);

    int insertUser(User record);

    int insertSelective(User record);

    int updateByUserSelective(User record);

    int updateByUser(User record);
}