package com.pet_clinic_end.mapper;

import com.pet_clinic_end.entity.Case;
import com.pet_clinic_end.entity.CaseDetail;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface CaseMapper {
    Integer addCase(Case c);
    Case getCaseById(@Param("caseId") Integer caseId);
    Integer addCaseItem(@Param("caseId") Integer caseId, @Param("itemId") Integer itemId, @Param("createTime") Date createTime, @Param("createUser") Integer createUser);
    Integer addCaseMedicine(@Param("caseId") Integer caseId, @Param("medicineId") Integer medicineId, @Param("createTime") Date createTime, @Param("createUser") Integer createUser);
    Integer addCaseDetail(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow, @Param("data") String data, @Param("createTime") Date createTime, @Param("createUser") String createUser);
    Integer updateCaseDetail(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow, @Param("data") String data, @Param("updateTime") Date updateTime, @Param("updateUser") String updateUser);
    CaseDetail getCaseDetailByCaseId(@Param("caseId") Integer caseId, @Param("dataCol") Integer dataCol, @Param("dataRow") Integer dataRow);
    List<Case> getCasePage(@Param("typeId") Integer typeId, @Param("caseName") String caseName, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);
    void deleteCaseById(@Param("did") Long did);
    void deleteCaseItemByCaseId(@Param("caseId") Long caseId);
    void deleteCaseMedicineByCaseId(@Param("caseId") Long caseId);
    void deleteCaseDetailByCaseId(@Param("caseId") Long caseId);
}
