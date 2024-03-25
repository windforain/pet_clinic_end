package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.UserExam;
import com.pet_clinic_end.mapper.UserExamMapper;
import com.pet_clinic_end.service.UserExamService;
import org.springframework.stereotype.Service;

@Service
public class UserExamServiceImpl extends ServiceImpl<UserExamMapper, UserExam> implements UserExamService {
}
