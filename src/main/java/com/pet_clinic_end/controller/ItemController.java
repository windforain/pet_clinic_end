package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.IdList;
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

    @PostMapping("/list")
    public Result<List<Item>> list()
    {
        log.info("ITEM LIST");
        List<Item> list = itemService.list();
        return Result.success(list);
    }

    @PostMapping("/detail")
    public Result<Item> detail(@RequestBody Item item)
    {
        log.info(item.toString());
        Item detail = itemService.detail(item);
        if (detail != null)
        {
            return Result.success(detail);
        }
        else
        {
            return Result.error("化验项目不存在");
        }

    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestBody IdList idList)
    {

        List<Long> ids = idList.getIds();
        itemService.delete(ids);
        return Result.success("删除化验项目成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name)
    {
        log.info(page + " " + pageSize + " " + name);
        Page<Item> questionPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Item::getName, name);
        queryWrapper.orderByDesc(Item::getUpdateTime);
        itemService.page(questionPage, queryWrapper);

        return Result.success(questionPage);
    }
}
