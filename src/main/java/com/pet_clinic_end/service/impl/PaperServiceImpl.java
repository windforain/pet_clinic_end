package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Paper;
import com.pet_clinic_end.mapper.PaperMapper;
import com.pet_clinic_end.service.PaperService;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
}
