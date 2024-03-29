package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.Market;
import com.pet_clinic_end.mapper.ItemMapper;
import com.pet_clinic_end.mapper.MarketMapper;
import com.pet_clinic_end.service.ItemService;
import com.pet_clinic_end.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {
}
