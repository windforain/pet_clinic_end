package com.pet_clinic_end.dto;

import com.pet_clinic_end.entity.Paper;
import lombok.Data;

import java.util.List;

@Data
public class PaperDto extends Paper {

    private Integer totalScore;

    private List<QuestionDto> questionDtoList;

}
