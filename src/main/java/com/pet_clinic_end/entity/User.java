package com.pet_clinic_end.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String image;
    @Getter
    private String email;
    @Getter
    @Setter
    private Integer role; // 0: Admin 1: Common user
    @Getter
    private String code; // used for register only, not exists in database
}