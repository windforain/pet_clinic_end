package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.common.ExamAndUserIds;
import com.pet_clinic_end.entity.Exam;
import com.pet_clinic_end.entity.ExamAuthority;
import com.pet_clinic_end.entity.QuestionPaper;
import com.pet_clinic_end.entity.UserExam;
import com.pet_clinic_end.mapper.ExamMapper;
import com.pet_clinic_end.service.ExamAuthorityService;
import com.pet_clinic_end.service.ExamService;
import com.pet_clinic_end.service.QuestionPaperService;
import com.pet_clinic_end.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private ExamAuthorityService examAuthorityService;

    @Autowired
    private QuestionPaperService questionPaperService;

    @Autowired
    private UserExamService userExamService;

    /**
     * 给考试添加考生
     * @param examAndUserIds
     */
    @Override
    @Transactional
    public void addUsersToExam(ExamAndUserIds examAndUserIds) {
        Long examId = examAndUserIds.getExamId();
        List<Long> userIds = examAndUserIds.getUserIds();
        for (Long userId: userIds) {
            ExamAuthority examAuthority = new ExamAuthority();
            examAuthority.setExamId(examId);
            examAuthority.setUserId(userId);
            examAuthorityService.save(examAuthority);
        }
    }

    /**
     * 用户提交考卷答案
     * @param userExamList
     */
    @Override
    @Transactional
    public void commitAnswers(List<UserExam> userExamList) {
        for (UserExam userExam: userExamList) {
            Long examId = userExam.getExamId();
            Exam exam = this.getById(examId);
            Long paperId = exam.getPaperId();
            LambdaQueryWrapper<QuestionPaper> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QuestionPaper::getPaperId, paperId);
            queryWrapper.eq(QuestionPaper::getQuestionNum, userExam.getQuestionNum());
            QuestionPaper questionPaper = questionPaperService.getOne(queryWrapper);
            userExam.setQuestionId(questionPaper.getQuestionId());
        }
        userExamService.saveBatch(userExamList);
    }
}
