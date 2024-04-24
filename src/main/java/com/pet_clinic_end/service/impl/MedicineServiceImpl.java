package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.ItemCase;
import com.pet_clinic_end.entity.Medicine;
import com.pet_clinic_end.entity.MedicineCase;
import com.pet_clinic_end.mapper.ItemMapper;
import com.pet_clinic_end.mapper.MedicineMapper;
import com.pet_clinic_end.service.ItemService;
import com.pet_clinic_end.service.MedicineCaseService;
import com.pet_clinic_end.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {
    @Autowired
    MedicineCaseService medicineCaseService;
    @Override
    public void add(Medicine medicine) {
        if (medicine.getId() != null)
        {
            this.removeById(medicine.getId());
        }
        this.save(medicine);
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean succ = true;
        for (Long id : ids)
        {
            LambdaQueryWrapper<MedicineCase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MedicineCase::getMedicineId, id);
            List<MedicineCase> list = medicineCaseService.list(lambdaQueryWrapper);
            if (!list.isEmpty())
            {
                succ = false;
                continue;
            }
            this.removeById(id);
        }
        return succ;
    }


}
