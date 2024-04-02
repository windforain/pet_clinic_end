package com.pet_clinic_end.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Case implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Integer id;
    @Getter
    private Integer typeId;
    @Getter
    private String caseName;
    @Getter
    @Setter
    private Date createTime;
    @Getter
    @Setter
    private Date updateTime;
    @Getter
    @Setter
    private Integer createUser;
    @Getter
    @Setter
    private Integer updateUser;
    @Getter
    private List<Long> itemId;
    @Getter
    private List<Long> medicineId;
}
