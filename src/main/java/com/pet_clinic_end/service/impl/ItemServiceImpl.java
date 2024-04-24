package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.ItemCase;
import com.pet_clinic_end.mapper.ItemMapper;
import com.pet_clinic_end.service.ItemCaseService;
import com.pet_clinic_end.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    ItemCaseService itemCaseService;
    @Override
    public void add(Item item) {
        if (item.getId() != null)
        {
            this.removeById(item.getId());
        }
        this.save(item);
    }

    @Override
    public List<Item> list() {
        LambdaQueryWrapper<Item> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Item> list = this.list(lambdaQueryWrapper);
        return list;
    }

    @Override
    public Item detail(Item item) {
        LambdaQueryWrapper<Item> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(item.getId() != null, Item::getId, item.getId());
        Item getOneItem = this.getOne(lambdaQueryWrapper);
        return getOneItem;
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean succ = true;
        for (Long id : ids)
        {
            LambdaQueryWrapper<ItemCase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ItemCase::getItemId, id);
            List<ItemCase> list = itemCaseService.list(lambdaQueryWrapper);
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
