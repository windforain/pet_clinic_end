package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.IdList;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.Medicine;
import com.pet_clinic_end.service.ItemService;
import com.pet_clinic_end.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicine")
@Slf4j
public class MedicineController {

    @Autowired
    private MedicineService medicineService;
    @PostMapping("/add")
    public Result<String> add(@RequestBody Medicine medicine)
    {
        log.info(medicine.toString());
        medicineService.add(medicine);
        return Result.success("新增药品成功");
    }

    @PostMapping("/list")
    public Result<List<Medicine>> list()
    {
        log.info("MEDICINE LIST");
        List<Medicine> list = medicineService.list();
        return Result.success(list);
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestBody IdList idList)
    {
        List<Long> ids = idList.getIds();
        medicineService.delete(ids);
        return Result.success("删除药品成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name)
    {

        Page<Medicine> questionPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Medicine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Medicine::getName, name);
        queryWrapper.orderByDesc(Medicine::getUpdateTime);
        medicineService.page(questionPage, queryWrapper);

        return Result.success(questionPage);
    }

}
