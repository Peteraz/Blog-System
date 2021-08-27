package com.example.blogsystemuserprovider.service.serviceImpl;

import com.example.blogsystemuserprovider.dao.UserMapper;
import com.example.blogsystemuserprovider.service.UserService;
import com.example.blogsystem.entity.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserById(String userid){
        return userMapper. getUserById(userid);
    }

    @Override
    public User getUserByAccountAndPassword(String account,String password){
        return userMapper. getUserByAccountAndPassword(account,password);
    }

    @Override
    public int deleteById(String userid){
        return userMapper.deleteById(userid);
    }

    @Override
    public int insert(User record){
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record){
        return userMapper.insertSelective(record);
    }

    @Override
    public int updateByUserSelective(User record){
        return userMapper.updateByUserSelective(record);
    }

    @Override
    public int updateById(User record){
        return userMapper.updateById(record);
    }
}
