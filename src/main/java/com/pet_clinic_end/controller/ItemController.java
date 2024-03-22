package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;
    @PostMapping("/add")
    public Result<String> add(@RequestBody Item item)
    {
        log.info(item.toString());
        itemService.add(item);
        return Result.success("新增化验项目成功");
    }

}
