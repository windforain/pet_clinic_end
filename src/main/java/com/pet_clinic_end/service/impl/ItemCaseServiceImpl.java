package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.ItemCase;
import com.pet_clinic_end.mapper.ItemCaseMapper;
import com.pet_clinic_end.service.ItemCaseService;
import org.springframework.stereotype.Service;

@Service
public class ItemCaseServiceImpl extends ServiceImpl<ItemCaseMapper, ItemCase> implements ItemCaseService {
}
