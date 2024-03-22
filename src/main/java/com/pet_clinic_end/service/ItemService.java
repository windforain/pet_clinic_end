package com.pet_clinic_end.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet_clinic_end.entity.Item;


public interface ItemService extends IService<Item> {
    public void add(Item item);
}
