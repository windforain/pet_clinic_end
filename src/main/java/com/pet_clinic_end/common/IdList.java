package com.pet_clinic_end.common;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class IdList {
    @Getter
    private List<Long> ids;
}
