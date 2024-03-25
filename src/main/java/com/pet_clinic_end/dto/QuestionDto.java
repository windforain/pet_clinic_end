package com.pet_clinic_end.dto;

import com.pet_clinic_end.entity.Question;
import lombok.Data;

@Data
public class QuestionDto extends Question {

    private String typeName;

    private Integer score;

    private Integer questionNum;
}
