package com.pet_clinic_end.dto;

import com.pet_clinic_end.entity.Exam;
import lombok.Data;

@Data
public class ExamDto extends Exam {

    private Integer examScore;
}
