package com.pet_clinic_end.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet_clinic_end.common.ExamAndUserIds;
import com.pet_clinic_end.common.SendMailUtil;
import com.pet_clinic_end.entity.*;
import com.pet_clinic_end.mapper.ExamMapper;
import com.pet_clinic_end.service.*;
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

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamService examService;

    /**
     * 给考试添加考生
     * @param examAndUserIds
     */
    @Override
    @Transactional
    public void addUsersToExam(ExamAndUserIds examAndUserIds) {
        Long examId = examAndUserIds.getExamId();
        Exam exam = examService.getById(examId);
        List<Long> userIds = examAndUserIds.getUserIds();
        for (Long userId: userIds) {
            User user0 = new User();
            user0.setId(userId);
            User user = userService.getUserById(user0);
            ExamAuthority examAuthority = new ExamAuthority();
            examAuthority.setExamId(examId);
            examAuthority.setUserId(userId);
            examAuthorityService.save(examAuthority);
            SendMailUtil sendMailUtil = new SendMailUtil();
            sendMailUtil.sendEmail(1, "19821851880@163.com", "BALMJGAIRSSUJBLB", user.getEmail(), null, exam);
        }
    }

    /**
     * 用户提交考卷答案
     * @param userExamList
     */
    @Override
    @Transactional
    public void commitAnswers(List<UserExam> userExamList) {
        Integer mark = 0;
        for (UserExam userExam: userExamList) {
            Long examId = userExam.getExamId();
            Exam exam = this.getById(examId);
            Long paperId = exam.getPaperId();
            LambdaQueryWrapper<QuestionPaper> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QuestionPaper::getPaperId, paperId);
            queryWrapper.eq(QuestionPaper::getQuestionNum, userExam.getQuestionNum());
            QuestionPaper questionPaper = questionPaperService.getOne(queryWrapper);
            Long questionId = questionPaper.getQuestionId();
            userExam.setQuestionId(questionId);
            Question question = questionService.getById(questionId);
            String questionAnswer = question.getAnswer();
            if (questionAnswer.equals(userExam.getUserAnswer())) {
                mark += questionPaper.getScore();
            }
        }
        userExamService.saveBatch(userExamList);

        Long userId = userExamList.get(0).getUserId();
        Long examId = userExamList.get(0).getExamId();
        ExamAuthority examAuthority = new ExamAuthority();
        examAuthority.setUserId(userId);
        examAuthority.setExamId(examId);
        examAuthority.setMark(mark);
        LambdaQueryWrapper<ExamAuthority> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAuthority::getExamId, examAuthority.getExamId());
        queryWrapper.eq(ExamAuthority::getUserId, examAuthority.getUserId());
        examAuthorityService.update(examAuthority, queryWrapper);
    }
}
