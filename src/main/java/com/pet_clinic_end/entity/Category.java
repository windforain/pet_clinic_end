package com.pet_clinic_end.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
}
