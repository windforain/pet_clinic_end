package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.ExamAuthority;
import com.pet_clinic_end.mapper.ExamAuthorityMapper;
import com.pet_clinic_end.service.ExamAuthorityService;
import org.springframework.stereotype.Service;

@Service
public class ExamAuthorityServiceImpl extends ServiceImpl<ExamAuthorityMapper, ExamAuthority> implements ExamAuthorityService {
}
