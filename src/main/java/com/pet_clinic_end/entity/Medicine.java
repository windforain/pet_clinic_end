package com.pet_clinic_end.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer price;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
