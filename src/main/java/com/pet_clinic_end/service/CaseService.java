package com.pet_clinic_end.service;

import com.pet_clinic_end.entity.Case;
import com.pet_clinic_end.entity.CaseDetail;
import com.pet_clinic_end.mapper.CaseMapper;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CaseService {
    @Autowired
    CaseMapper caseMapper;
    public Integer addCase(Case c) {
        return caseMapper.addCase(c);
    }
    public Case getCaseById(Integer caseId){
        return caseMapper.getCaseById(caseId);
    }
    public Integer addCaseItem(Integer caseId, Integer itemId, Date createTime, Integer createUser){
        return caseMapper.addCaseItem(caseId,itemId,createTime,createUser);
    }
    public Integer addCaseMedicine(Integer caseId, Integer medicineId, Date createTime, Integer createUser){
        return caseMapper.addCaseMedicine(caseId,medicineId,createTime,createUser);
    }
    public Integer updateCaseDetail(Integer caseId, Integer dataCol, Integer dataRow, String data, Date createTime, String createUser){
        CaseDetail cd = caseMapper.getCaseDetailByCaseId(caseId,dataCol,dataRow);
        if (cd == null) {
            return caseMapper.addCaseDetail(caseId,dataCol,dataRow,data,createTime,createUser);
        }
        // actually updateTime&updateUser
        return caseMapper.updateCaseDetail(caseId,dataCol,dataRow,data,createTime,createUser);
    }
    public List<Case> getCasePage(Integer typeId, String caseName, Integer begin, Integer pageSize){
        return caseMapper.getCasePage(typeId,caseName,begin,pageSize);
    }
    public void deleteCaseById(Long did){
        caseMapper.deleteCaseById(did);
        caseMapper.deleteCaseItemByCaseId(did);
        caseMapper.deleteCaseMedicineByCaseId(did);
        caseMapper.deleteCaseDetailByCaseId(did);
    }
    public Object getCaseDetailByCaseId(Integer caseId, Integer dataCol) {
        String caseName = "";
        String text = "";
        List<String> picture = new ArrayList<>();
        List<String> video = new ArrayList<>();
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

        Map<String, Object> detail = new HashMap<>();
        detail.put("text",text);
        detail.put("picture",picture);
        detail.put("video",video);
        Map<String, Object> data = new HashMap<>();
        data.put("caseName", caseName);
        data.put("detail", detail);

        return data;
    }
}
