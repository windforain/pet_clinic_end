package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.Medicine;
import com.pet_clinic_end.mapper.ItemMapper;
import com.pet_clinic_end.mapper.MedicineMapper;
import com.pet_clinic_end.service.ItemService;
import com.pet_clinic_end.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {
    @Override
    public void add(Medicine medicine) {
        if (medicine.getId() != null)
        {
            this.removeById(medicine.getId());
        }
        this.save(medicine);
    }

    @Override
    public void delete(List<Long> ids) {
        this.removeByIds(ids);
    }


}
