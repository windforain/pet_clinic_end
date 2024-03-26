package com.pet_clinic_end.controller;


import com.pet_clinic_end.common.Idlist;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.Case;
import com.pet_clinic_end.entity.CaseDetail;
import com.pet_clinic_end.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Timestamp;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/case")
@Slf4j
public class CaseController {
    @Autowired
    CaseService caseService;

    @PostMapping("/add")
    public Result<String> add(@RequestBody Case c) {
        //    {
        //        "typeId": 0,
        //            "itemId": [
        //        "string"
        //    ],
        //        "medicineId": [
        //        "string"
        //    ],
        //        "caseName": "string"
        //    }
        Integer typeId = c.getTypeId();
        String caseName = c.getCaseName();
        Date date = new Date();
        c.setCreateTime(date);
        // TODO: Add createUser
        c.setCreateUser(null);
        Integer createUser = null;

        Integer result = caseService.addCase(c);
        if (result!=1) {
            return Result.error("添加病例失败");
        }
        Integer cid = c.getId();
        for (Integer iid : c.getItemId()) {
            result = caseService.addCaseItem(cid, iid, date, createUser);
            if (result!=1) {
                return Result.error("添加病例-化验项目关系失败");
            }
        }
        for (Integer mid : c.getMedicineId()) {
            result = caseService.addCaseMedicine(cid, mid, date, createUser);
            if (result!=1) {
                return Result.error("添加病例-药品关系失败");
            }
        }
        return Result.success("添加病例成功");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody CaseDetail cd){
//        {
//            "case_id": 0,
//                "data_type": 0,
//                "text": "string",
//                "picture": [
//            "string"
//    ],
//            "video": [
//            "string"
//    ]
//        }
        Integer caseId = cd.getCaseId();
        Integer dataCol = cd.getDataType();
        Date date = new Date();
        // TODO
        String createUser = null;

        Case existCase = caseService.getCaseById(caseId);
        if (existCase==null) {
            return Result.error("当前病例不存在，无法添加病例信息");
        }
        Integer result = caseService.updateCaseDetail(caseId,dataCol,0,cd.getText(),date,createUser);
        if (result!=1) {
            return Result.error("添加病例文字信息失败");
        }
        for (String pic: cd.getPicture()) {
            result = caseService.updateCaseDetail(caseId,dataCol,1,pic,date,createUser );
            if (result!=1) {
                return Result.error("添加病例图片信息失败");
            }
        }
        for (String vid: cd.getVideo()) {
            result = caseService.updateCaseDetail(caseId,dataCol,2,vid,date,createUser);
            if (result!=1) {
                return Result.error("添加病例视频信息失败");
            }
        }
        return Result.success("添加病例信息成功");
    }

    @GetMapping("/page")
    public Result<Object> page(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) Integer typeId, @RequestParam(required = false) String caseName) {
        //        {
//            "page": 0,
//            "pageSize": 10,
//            "typeId": "0" （optional）,
//            "caseName": "寄生虫"  (optional)
//        }
        if (page<0 || pageSize<=0) {
            return Result.error("分页参数错误");
        }
        Integer begin = page * pageSize;
        List<Case> pageCase = caseService.getCasePage(typeId,caseName,begin,pageSize);
        Integer total = pageCase.size();
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageCase);
        data.put("total", total);
        return Result.success(data);
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestBody Idlist idList){
//        {
//            "case_id": [
//            0
//    ]
//        }
        List<Long> ids = idList.getIds();
        for (Long did: ids){
            try {
                // delete case and its relationships from case, item_case, medicine_case, case_detail
                caseService.deleteCaseById(did);
            } catch (Exception e) {
                return Result.error("删除病例id"+did.toString()+"失败");
            }
        }
        return Result.success("删除病例成功");
    }

    @PostMapping("/detail")
    public  Result<Object> detail(@RequestBody CaseDetail cd) {
//        {
//            "case_id": 0
//            "dataType": 0
//        }
        Integer caseId = cd.getCaseId();
        Integer dataCol = cd.getDataType();
        Object data = null;
        try {
            data = caseService.getCaseDetailByCaseId(caseId,dataCol);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取病例详情失败");
        }
        return Result.success(data);
    }
}

