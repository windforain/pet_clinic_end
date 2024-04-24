package com.pet_clinic_end.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.entity.Case;
import com.pet_clinic_end.entity.CaseDetail;
import com.pet_clinic_end.entity.Category;
import com.pet_clinic_end.entity.Type;
import com.pet_clinic_end.mapper.CaseMapper;
import com.pet_clinic_end.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Case> implements CaseService {
    @Autowired
    CaseMapper caseMapper;
    @Override
    public Integer addCase(Case c) {
        return caseMapper.addCase(c);
    }
    @Override
    public Case getCaseById(Integer caseId){
        return caseMapper.getCaseById(caseId);
    }
    @Override
    public Integer addCaseItem(Long caseId, Long itemId, Date createTime, Integer createUser){
        return caseMapper.addCaseItem(caseId,itemId,createTime,createUser);
    }
    @Override
    public Integer addCaseMedicine(Long caseId, Long medicineId, Date createTime, Integer createUser){
        return caseMapper.addCaseMedicine(caseId,medicineId,createTime,createUser);
    }
    @Override
    public Integer updateCaseDetail(Integer caseId, Integer dataCol, Integer dataRow, String data, Date createTime, String createUser){
        return caseMapper.addCaseDetail(caseId,dataCol,dataRow,data,createTime,createUser);
    }
    @Override
    public List<Case> getCasePage(Integer typeId, String caseName, Integer begin, Integer pageSize){
        return caseMapper.getCasePage(typeId,caseName,begin,pageSize);
    }
    @Override
    public Integer getTotalCase(){
        return caseMapper.getTotalCase();
    }
    @Override
    public void deleteCaseDetail(Integer caseId, Integer dataCol, Integer dataRow) {
        caseMapper.deleteCaseDetail(caseId,dataCol,dataRow);
    }
    @Override
    public void deleteCaseById(Long did){
        caseMapper.deleteCaseById(did);
        caseMapper.deleteCaseItemByCaseId(did);
        caseMapper.deleteCaseMedicineByCaseId(did);
        caseMapper.deleteCaseDetailByCaseId(did);
    }
    @Override
    public Object getCaseDetailByCaseId(Integer caseId, Integer dataCol) {
        String caseName = "";
        String text = "";
        List<String> picture = new ArrayList<>();
        List<String> video = new ArrayList<>();
        List<String> itemIdList = new ArrayList<>();
        List<String> medicineIdList = new ArrayList<>();
        // text: row=0
        CaseDetail textDetail = caseMapper.getCaseDetailByCaseId(caseId,dataCol,0);
        if (textDetail!=null) {
            caseName = textDetail.getCaseName();
            text = textDetail.getDetail();
        }
        // picture: row=1
        CaseDetail pictureDetail = caseMapper.getCaseDetailByCaseId(caseId,dataCol,1);
        if (pictureDetail!=null) {
            String pictureString = pictureDetail.getDetail();
            picture = Arrays.asList(pictureString.split(","));
        }
        // video: row=2
        CaseDetail videoDetail = caseMapper.getCaseDetailByCaseId(caseId,dataCol,2);
        if (videoDetail!=null) {
            String videoString = videoDetail.getDetail();
            video = Arrays.asList(videoString.split(","));
        }
        String itemIds = caseMapper.getRelatedItemByCaseId(caseId);
        if (itemIds!=null) {
            itemIdList = Arrays.asList(itemIds.split(","));
        }
        String medicineIds = caseMapper.getRelatedMedicineByCaseId(caseId);
        if (medicineIds!=null) {
            medicineIdList = Arrays.asList(medicineIds.split(","));
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("text",text);
        detail.put("picture",picture);
        detail.put("video",video);
        Map<String, Object> data = new HashMap<>();
        data.put("caseName", caseName);
        data.put("detail", detail);
        data.put("itemIds", itemIdList);
        data.put("medicineIds", medicineIdList);

        return data;
    }
    @Override
    public List<Category> getCategorys(){
        return caseMapper.getCategorys();
    }
    @Override
    public List<Type> getTypesByCategoryId(Integer categoryId){
        return caseMapper.getTypesByCategoryId(categoryId);
    }
    @Override
    public List<CaseDetail> queryCaseDetail(String query) {
        return caseMapper.queryCaseDetail(query);
    }
}