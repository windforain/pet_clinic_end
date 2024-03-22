package com.pet_clinic_end.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long itemId;
    private String name;
    private Integer price;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
