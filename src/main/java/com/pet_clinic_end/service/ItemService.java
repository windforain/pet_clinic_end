package com.pet_clinic_end.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet_clinic_end.entity.Item;

import java.util.List;


public interface ItemService extends IService<Item> {
    public void add(Item item);

    public List<Item> list();

    public Item detail(Item item);

    public boolean delete(List<Long> ids);
}
