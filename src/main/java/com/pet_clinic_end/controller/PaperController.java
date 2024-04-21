package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.IdList;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.dto.PaperDto;
import com.pet_clinic_end.dto.QuestionDto;
import com.pet_clinic_end.entity.Exam;
import com.pet_clinic_end.entity.Paper;
import com.pet_clinic_end.entity.Question;
import com.pet_clinic_end.entity.QuestionPaper;
import com.pet_clinic_end.service.ExamService;
import com.pet_clinic_end.service.PaperService;
import com.pet_clinic_end.service.QuestionPaperService;
import com.pet_clinic_end.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paper")
@Slf4j
public class PaperController {

    @Autowired
    PaperService paperService;

    @Autowired
    QuestionPaperService questionPaperService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamService examService;

    /**
     * 添加/修改试卷
     * @param paper
     * @return
     */
    @PostMapping("/update")
    @CacheEvict(value = "paperCache", allEntries = true)
    public Result<String> update(@RequestBody Paper paper) {
        Long paperId = paper.getId();
        if (paperId != null) {
            paperService.updateById(paper);
            return Result.success("修改试卷成功");
        }
        paperService.save(paper);
        return Result.success("添加试卷成功");
    }

    /**
     * 分页获取试卷列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @Cacheable(value = "paperCache", key = "#page + '_' + #pageSize + '_' + #name")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Paper> paperPage = new Page<>(page, pageSize);
        Page<PaperDto> paperDtoPage = new Page<>();

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Paper::getName, name);
        queryWrapper.orderByDesc(Paper::getUpdateTime);
        paperService.page(paperPage, queryWrapper);

        BeanUtils.copyProperties(paperPage, paperDtoPage, "records");
        List<Paper> paperList = paperPage.getRecords();
        List<PaperDto> paperDtoList = new ArrayList<>();
        for (Paper paper: paperList) {
            PaperDto paperDto = new PaperDto();
            BeanUtils.copyProperties(paper, paperDto);
            Long paperId = paper.getId();
            LambdaQueryWrapper<QuestionPaper> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(QuestionPaper::getPaperId, paperId);
            List<QuestionPaper> list = questionPaperService.list(lambdaQueryWrapper);
            Integer totScore = 0;
            for (QuestionPaper questionPaper: list) {
                totScore += questionPaper.getScore();
            }
            paperDto.setTotalScore(totScore);
            paperDtoList.add(paperDto);
        }

        paperDtoPage.setRecords(paperDtoList);
        return Result.success(paperDtoPage);
    }

    /**
     * 删除试卷
     * @param idList
     * @return
     */
    @DeleteMapping("/delete")
    @CacheEvict(value = "paperCache", allEntries = true)
    public Result<String> delete(@RequestBody IdList idList) {
        List<Long> ids = idList.getIds();
        for (Long id: ids) {
            LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Exam::getPaperId, id);
            List<Exam> list = examService.list(queryWrapper);
            if (!list.isEmpty()) {
                return Result.error("部分试卷无法删除，请确保考试中不使用待删除的试卷");
            }
        }
        paperService.removeByIds(ids);
        return Result.success("删除试卷成功");
    }

    /**
     * 在试卷中添加试题
     * @param questionPaperList
     * @return
     */
    @PostMapping("/addQuestion")
    @CacheEvict(value = "paperCache", allEntries = true)
    public Result<String> addQuestion(@RequestBody List<QuestionPaper> questionPaperList) {
        LambdaQueryWrapper<QuestionPaper> queryWrapper = new LambdaQueryWrapper<>();
        Long paperId = questionPaperList.get(0).getPaperId();
        queryWrapper.eq(QuestionPaper::getPaperId, paperId);
        questionPaperService.remove(queryWrapper);
        questionPaperService.saveBatch(questionPaperList);
        return Result.success("在试卷中添加试题成功");
    }

    /**
     * 获取试卷详情
     * @param paper1
     * @return
     */
    @PostMapping("/detail")
    public Result<PaperDto> detail(@RequestBody Paper paper1) {
        Long paperId = paper1.getId();
        Paper paper = paperService.getById(paperId);
        PaperDto paperDto = new PaperDto();
        BeanUtils.copyProperties(paper, paperDto);

        LambdaQueryWrapper<QuestionPaper> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QuestionPaper::getPaperId, paperId);
        lambdaQueryWrapper.orderByAsc(QuestionPaper::getQuestionNum);
        List<QuestionPaper> list = questionPaperService.list(lambdaQueryWrapper);
        Integer totScore = 0;
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (QuestionPaper questionPaper: list) {
            totScore += questionPaper.getScore();
            QuestionDto questionDto = new QuestionDto();
            Question question = questionService.getById(questionPaper.getQuestionId());
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setQuestionNum(questionPaper.getQuestionNum());
            questionDto.setScore(questionPaper.getScore());
            questionDtoList.add(questionDto);
        }

        paperDto.setTotalScore(totScore);
        paperDto.setQuestionDtoList(questionDtoList);
        return Result.success(paperDto);
    }
}
