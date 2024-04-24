package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Exam;
import com.pet_clinic_end.entity.File;
import com.pet_clinic_end.mapper.ExamMapper;
import com.pet_clinic_end.mapper.FileMapper;
import com.pet_clinic_end.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
}
