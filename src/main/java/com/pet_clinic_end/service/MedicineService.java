package com.pet_clinic_end.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet_clinic_end.entity.Medicine;

import java.util.List;


public interface MedicineService extends IService<Medicine> {
    public void add(Medicine medicine);


    public void delete(List<Long> ids);
}
