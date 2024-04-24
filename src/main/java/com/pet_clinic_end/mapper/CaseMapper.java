package com.pet_clinic_end.mapper;

import com.pet_clinic_end.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface CaseMapper extends BaseMapper<Case>{
    Integer addCase(Case c);
    Case getCaseById(@Param("caseId") Integer caseId);
    Integer addCaseItem(@Param("caseId") Long caseId, @Param("itemId") Long itemId, @Param("createTime") Date createTime, @Param("createUser") Integer createUser);
    Integer addCaseMedicine(@Param("caseId") Long caseId, @Param("medicineId") Long medicineId, @Param("createTime") Date createTime, @Param("createUser") Integer createUser);
    Integer addCaseDetail(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow, @Param("data") String data, @Param("createTime") Date createTime, @Param("createUser") String createUser);
    Integer updateCaseDetail(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow, @Param("data") String data, @Param("updateTime") Date updateTime, @Param("updateUser") String updateUser);
    CaseDetail getCaseDetailByCaseId(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow);
    String getRelatedItemByCaseId(@Param("caseId") Integer caseId);
    String getRelatedMedicineByCaseId(@Param("caseId") Integer caseId);
    List<Case> getCasePage(@Param("typeId") Integer typeId, @Param("caseName") String caseName, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);
    Integer getTotalCase();
    void deleteCaseDetail(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow);
    void deleteCaseById(@Param("did") Long did);
    void deleteCaseItemByCaseId(@Param("caseId") Long caseId);
    void deleteCaseMedicineByCaseId(@Param("caseId") Long caseId);
    void deleteCaseDetailByCaseId(@Param("caseId") Long caseId);
    List<Category> getCategorys();
    List<Type> getTypesByCategoryId(@Param("categoryId") Integer categoryId);
    List<CaseDetail> queryCaseDetail(@Param("query") String query);
}
