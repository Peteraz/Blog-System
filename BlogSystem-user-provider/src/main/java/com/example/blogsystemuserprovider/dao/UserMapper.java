package com.example.blogsystemuserprovider.dao;

import com.example.blogsystem.entity.User;
import com.example.blogsystem.entity.UserExample;
import com.example.blogsystem.entity.UserKey;
import com.example.blogsystem.entity.UserWithBLOBs;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//@Mapper就是告诉spring这是你指定的mapper
@Mapper
public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(UserKey key);

    int insert(UserWithBLOBs record);

    int insertSelective(UserWithBLOBs record);

    List<UserWithBLOBs> selectByExampleWithBLOBs(UserExample example);

    List<User> selectByExample(UserExample example);

    UserWithBLOBs selectByPrimaryKey(UserKey key);

    int updateByExampleSelective(@Param("record") UserWithBLOBs record, @Param("example") UserExample example);

    int updateByExampleWithBLOBs(@Param("record") UserWithBLOBs record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(UserWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(UserWithBLOBs record);

    int updateByPrimaryKey(User record);
}