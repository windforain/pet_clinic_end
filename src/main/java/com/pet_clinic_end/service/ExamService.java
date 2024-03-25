package com.pet_clinic_end.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet_clinic_end.common.ExamAndUserIds;
import com.pet_clinic_end.entity.Exam;
import com.pet_clinic_end.entity.UserExam;

import java.util.List;

public interface ExamService extends IService<Exam> {

    /**
     * 给考试添加考生
     * @param examAndUserIds
     */
    public void addUsersToExam(ExamAndUserIds examAndUserIds);

    /**
     * 提交考卷答案
     * @param userExamList
     */
    public void commitAnswers(List<UserExam> userExamList);
}
