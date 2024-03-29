package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Department;
import com.pet_clinic_end.entity.ExamAuthority;
import com.pet_clinic_end.mapper.DepartmentMapper;
import com.pet_clinic_end.mapper.ExamAuthorityMapper;
import com.pet_clinic_end.service.DepartmentService;
import com.pet_clinic_end.service.ExamAuthorityService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
