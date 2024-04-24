package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.MedicineCase;
import com.pet_clinic_end.mapper.MedicineCaseMapper;
import com.pet_clinic_end.service.MedicineCaseService;
import org.springframework.stereotype.Service;

@Service
public class MedicineCaseServiceImpl extends ServiceImpl<MedicineCaseMapper, MedicineCase> implements MedicineCaseService {
}
