package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.common.SendMailUtil;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    UserService userService;

    @PostMapping("/code")
    public Result<String> code(@RequestBody User user){
//        {
//            "email": "string"
//        }
        String email = user.getEmail();
        User alreadyUser = userService.getUserByEmail(email);
        // 首先判断邮箱是否已经注册，已经注册则提示已经注册过了，未注册则继续
        if (null != alreadyUser){
            return Result.error("此邮箱已经注册过了");
        }
        // 定义返回消息
        String message;
        try {
            SendMailUtil sendMailUtil = new SendMailUtil();
            //判断邮箱格式是否正确
            if (sendMailUtil.isEmail(email)) {
                // 生成6位随机码
                StringBuilder code = sendMailUtil.CreateCode();
                ValueOperations<String, String> operations = redisTemplate.opsForValue();
                // 如果redis的验证码还存在则提示
                if (operations.get(email) != null) {
                    message = "验证码已发送,请60秒后重试";
                } else {
                    operations.set(email, String.valueOf(code));
                    // 设置验证码过期时间
                    redisTemplate.expire(email, 60, TimeUnit.SECONDS);
                    message = sendMailUtil.sendEmail("19821851880@163.com", "BALMJGAIRSSUJBLB", email, String.valueOf(code));
                    return Result.success(message);
                }
            } else {
                message = "邮箱格式不正确";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "出现未知错误";
        }
        return Result.error(message);
    }


    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> register(@RequestBody User user) {
////        {
////            "email": "string",
////            "username": "string",
////            "password": "string",
////            "code": "string"
////        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String email = user.getEmail();
        // 获取redis中的验证码
        String code = operations.get(email);
        User alreadyUser = userService.getUserByEmail(email);
        if (alreadyUser!= null) {
            return Result.error("邮箱已经注册");
        } else {
            // 如果验证码正确
            String veriCode = user.getCode();
            if (code!=null && code.equals(veriCode)){
                // 进行正常注册流程
                // 删除验证码，保证验证码使用一次
                redisTemplate.delete(email);
                // 往数据库添加用户（普通）
                //
                userService.addCommonUser(email, user.getName(), user.getPassword());
                return Result.success("注册成功");
            }else {
                return Result.error("验证码无效，请重新获取");
            }
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<Object> login(@RequestBody User user, HttpSession httpSession)
    {
//        {
//            "email": "string",
//            "password": "string"
//        }
        String email = user.getEmail();
        User selectUser = userService.getUserByEmail(email);
        String pwd = selectUser.getPassword();
        if (!pwd.equals(user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        httpSession.setAttribute("email", email);
        String session = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("id", selectUser.getId());
        data.put("session", session);
        return Result.success(data);
    }


    @GetMapping("/page")
    public Result<Object> page(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String username) {
//        {
//            "page": 0,
//            "pageSize": 10,
//            "username": "haha" （optional）
//        }
        if (page<0 || pageSize<=0) {
            return Result.error("分页参数错误");
        }
        Integer begin = page * pageSize;
        List<User> pageUser = userService.getUserPage(begin, pageSize, username);
        Integer total = pageUser.size();
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageUser);
        data.put("total", total);
        return Result.success(data);
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody User user) {
//        {
//            "id": 0,
//                "name": "string",
//                "password": "string",
//                "image": "string",
//                "email": "string"
//        }
        Integer result = userService.updateUserById(user);
        if (result!=1) {
            return Result.error("更新用户信息失败");
        }
        return Result.success("更新用户信息成功");
    }

    @PostMapping("/getUserInfo")
    public Result<Object> getUserInfo(@RequestBody User user) {
//        {
//            "id": 0
//        }
        User selectUser = userService.getUserById(user);
        if (selectUser==null) {
            Result.error("没有查询到该用户");
        }
        return Result.success(selectUser);
    }

}
