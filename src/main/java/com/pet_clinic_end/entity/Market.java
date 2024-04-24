package com.pet_clinic_end.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Market implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long marketId;

    private Long departmentId;
    private String id;
    private String tooltip;
    private String html;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String anchor;
    private Integer jump;
    private Integer target;
    private Integer angel;
    private String video;
    private String func;
    private String operator;

}
