package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.mapper.UserMapper;
import com.pet_clinic_end.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }
    @Override
    public void addCommonUser(String email, String name, String password) {
        userMapper.addCommonUser(email, name, password);
    }
    @Override
    public List<User> getUserPage(Integer begin, Integer pageSize, String name){
        return userMapper.getUserPage(begin, pageSize, name);
    }
    @Override
    public Integer getTotalUser(){
        return userMapper.getTotalUser();
    }
    @Override
    public Integer updateUserById(User user){
        return userMapper.updateUserById(user);
    }
    @Override
    public User getUserById(User user) {
        return userMapper.getUserById(user);
    }
}