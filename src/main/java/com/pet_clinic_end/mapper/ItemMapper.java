package com.pet_clinic_end.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet_clinic_end.entity.Item;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
}
