package com.pet_clinic_end.entity;


import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String password;
    //    private String image;
    @Getter
    private String email;
    @Getter
    private Integer role; // 0: Admin 1: Common user
    @Getter
    private String code; // used for register only, not exists in database
}