package com.pet_clinic_end.mapper;

import com.pet_clinic_end.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
//    User getUserByEmail(@Param("email") String email);
    User getUserByEmail(@Param("email") String email);
    void addCommonUser(@Param("email") String email, @Param("name") String name, @Param("password") String password);
    List<User> getUserPage(@Param("begin") Integer begin, @Param("pageSize") Integer pageSize, @Param("name") String name);
}
