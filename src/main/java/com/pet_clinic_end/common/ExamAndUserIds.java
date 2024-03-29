package com.pet_clinic_end.common;

import lombok.Data;

import java.util.List;

@Data
public class ExamAndUserIds {

    private Long examId;

    private List<Long> userIds;

}
