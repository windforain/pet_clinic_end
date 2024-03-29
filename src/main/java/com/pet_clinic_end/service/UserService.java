package com.pet_clinic_end.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService extends IService<User> {
    public User getUserByEmail(String email);
    public void addCommonUser(String email, String name, String password);
    public List<User> getUserPage(Integer begin, Integer pageSize, String name);
    public Integer updateUserById(User user);
    public User getUserById(User user);
}


//@Service
//@Transactional
//public class UserService {
//    @Autowired
//    UserMapper userMapper;
//    public User getUserByEmail(String email) {
//        return userMapper.getUserByEmail(email);
//    }
//    public void addCommonUser(String email, String name, String password) {
//        userMapper.addCommonUser(email, name, password);
//    }
//    public List<User> getUserPage(Integer begin, Integer pageSize, String name){
//        return userMapper.getUserPage(begin, pageSize, name);
//    }
//
//    public Integer updateUserById(User user){
//        return userMapper.updateUserById(user);
//    }
//    public User getUserById(User user) {
//        return userMapper.getUserById(user);
//    }
//}
