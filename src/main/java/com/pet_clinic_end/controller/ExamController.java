package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.ExamAndUserIds;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.dto.ExamDto;
import com.pet_clinic_end.entity.*;
import com.pet_clinic_end.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionPaperService questionPaperService;

    @Autowired
    private ExamAuthorityService examAuthorityService;

    @Autowired
    private UserExamService userExamService;

    @Autowired
    private UserService userService;

    /**
     * 添加/修改考试
     * @param exam
     * @return
     */
    @PostMapping("/update")
    @CacheEvict(value = "examCache", allEntries = true)
    public Result<String> update(@RequestBody Exam exam) {
        Long examId = exam.getId();
        if (examId != null) {
            examService.updateById(exam);
            return Result.success("修改考试成功");
        }
        examService.save(exam);
        return Result.success("添加考试成功");
    }

    /**
     * 分页获取考试列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @Cacheable(value = "examCache", key = "#page + '_' + #pageSize + '_' + #name")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Exam> examPage = new Page<>(page, pageSize);
        Page<ExamDto> examDtoPage = new Page<>();

        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Exam::getName, name);
        queryWrapper.orderByDesc(Exam::getUpdateTime);
        examService.page(examPage, queryWrapper);

        BeanUtils.copyProperties(examPage, examDtoPage, "records");
        List<Exam> examList = examPage.getRecords();
        List<ExamDto> examDtoList = new ArrayList<>();
        for (Exam exam: examList) {
            ExamDto examDto = new ExamDto();
            BeanUtils.copyProperties(exam, examDto);
            Long paperId = examDto.getPaperId();
            LambdaQueryWrapper<QuestionPaper> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(QuestionPaper::getPaperId, paperId);
            List<QuestionPaper> list = questionPaperService.list(lambdaQueryWrapper);
            Integer totScore = 0;
            for (QuestionPaper questionPaper: list) {
                totScore += questionPaper.getScore();
            }
            examDto.setExamScore(totScore);
            examDtoList.add(examDto);
        }

        examDtoPage.setRecords(examDtoList);
        return Result.success(examDtoPage);
    }

    /**
     * 给考试添加考生
     * @param examAndUserIds
     * @return
     */
    @PostMapping("/addUser")
    public Result<String> addUser(@RequestBody ExamAndUserIds examAndUserIds) {
        examService.addUsersToExam(examAndUserIds);
        return Result.success("给考试添加考生成功");
    }

    /**
     * 查询用户的考试情况，-2表示没有考试权限，-1表示有考试权限但还没考试，其他数字表示考试分数
     * @param examId
     * @param userId
     * @return
     */
    @GetMapping("/authority")
    public Result<Integer> authority(Long examId, Long userId) {
        LambdaQueryWrapper<ExamAuthority> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAuthority::getExamId, examId);
        queryWrapper.eq(ExamAuthority::getUserId, userId);
        List<ExamAuthority> list = examAuthorityService.list(queryWrapper);
        if (list.size() > 0) {
            return Result.success(list.get(0).getMark());
        }
        return Result.success(-2);
    }


    /**
     * 给考生打分
     * @param examAuthority
     * @return
     */
    /*
    @PostMapping("/grade")
    public Result<String> grade(@RequestBody ExamAuthority examAuthority) {
        LambdaQueryWrapper<ExamAuthority> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAuthority::getExamId, examAuthority.getExamId());
        queryWrapper.eq(ExamAuthority::getUserId, examAuthority.getUserId());
        examAuthorityService.update(examAuthority, queryWrapper);
        return Result.success("打分成功");
    }
    */

    /**
     * 用户提交考卷答案，同时进行判分
     * @param userExamList
     * @return
     */
    @PostMapping ("/commit")
    public Result<String> commit(@RequestBody List<UserExam> userExamList) {
        examService.commitAnswers(userExamList);
        return Result.success("提交答案成功");
    }

    /**
     * 获取考生在考试中的答案
     * @param userId
     * @param examId
     * @return
     */
    @GetMapping("/getUserAnswers")
    public Result<List<UserExam>> getUserAnswers(Long userId, Long examId) {
        LambdaQueryWrapper<UserExam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExam::getUserId, userId);
        queryWrapper.eq(UserExam::getExamId, examId);
        queryWrapper.orderByAsc(UserExam::getQuestionNum);
        List<UserExam> userExamList = userExamService.list(queryWrapper);
        return Result.success(userExamList);
    }

    /**
     * 获取考试的考生列表
     * @param examId
     * @return
     */
    @GetMapping("/getUsersOfExam")
    public Result<List<User>> getUsersOfExam(Long examId) {
        LambdaQueryWrapper<ExamAuthority> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAuthority::getExamId, examId);
        List<ExamAuthority> examAuthorityList = examAuthorityService.list(queryWrapper);
        List<User> userList = new ArrayList<>();
        for (ExamAuthority examAuthority: examAuthorityList) {
            Long userId = examAuthority.getUserId();
            User user0 = new User();
            user0.setId(userId);
            User user = userService.getUserById(user0);
            userList.add(user);
        }
        return Result.success(userList);
    }

}
