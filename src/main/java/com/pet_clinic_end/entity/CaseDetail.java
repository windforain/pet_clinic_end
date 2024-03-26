package com.pet_clinic_end.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
public class CaseDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Integer caseId;
    @Getter
    @Setter
    private String caseName;
    @Getter
    @Setter
    private Integer dataType;
    @Getter
    @Setter
    private String text;
    @Getter
    @Setter
    private List<String> picture;
    @Getter
    @Setter
    private List<String> video;
    // used for query
    @Getter
    @Setter
    private String detail;
}
