package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guide")
@Slf4j
public class GuideController {
    @Autowired
    private UserService userService;

    @PostMapping("/role")
    public Result<String> add(@RequestBody User user)
    {
        log.info(user.toString());
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(user.getId() != null, User::getId, user.getId());
        lambdaUpdateWrapper.set(user.getRole() != null, User::getRole, user.getRole());
        userService.update(lambdaUpdateWrapper);
        return Result.success("切换角色成功");
    }


}
