package com.pet_clinic_end.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.IdList;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.dto.QuestionDto;
import com.pet_clinic_end.entity.Question;
import com.pet_clinic_end.entity.QuestionPaper;
import com.pet_clinic_end.entity.Type;
import com.pet_clinic_end.service.QuestionPaperService;
import com.pet_clinic_end.service.QuestionService;
import com.pet_clinic_end.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private QuestionPaperService questionPaperService;

    /**
     * 添加/修改试题
     * @param question
     * @return
     */
    @PostMapping("/update")
    @CacheEvict(value = "questionCache", allEntries = true)
    public Result<String> update(@RequestBody Question question) {
        Long questionId = question.getId();
        if (questionId != null) {
            questionService.updateById(question);
            return Result.success("修改试题成功");
        }
        questionService.save(question);
        return Result.success("添加试题成功");
    }

    /**
     * 分页获取试题列表
     * @param page
     * @param pageSize
     * @param typeId
     * @param title
     * @return
     */
    @GetMapping("/page")
    @Cacheable(value = "questionCache", key = "#page + '_' + #pageSize + '_' + #typeId + '_' + #title")
    public Result<Page> page(int page, int pageSize, Integer typeId, String title) {
        Page<Question> questionPage = new Page<>(page, pageSize);
        Page<QuestionDto> questionDtoPage = new Page<>();

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(typeId != null, Question::getTypeId, typeId);
        queryWrapper.like(title != null, Question::getTitle, title);
        queryWrapper.orderByDesc(Question::getUpdateTime);
        questionService.page(questionPage, queryWrapper);

        BeanUtils.copyProperties(questionPage, questionDtoPage, "records");
        List<Question> questionList = questionPage.getRecords();
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question: questionList) {
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            Type type = typeService.getById(question.getTypeId());
            questionDto.setTypeName(type.getName());
            questionDtoList.add(questionDto);
        }

        questionDtoPage.setRecords(questionDtoList);
        return Result.success(questionDtoPage);
    }

    /**
     * 查询试题详情
     * @param question
     * @return
     */
    @PostMapping("/detail")
        public Result<Question> detail(@RequestBody Question question) {
        Long questionId = question.getId();
        Question question1 = questionService.getById(questionId);
        return Result.success(question1);
    }

    /**
     * 删除试题
     * @param idList
     * @return
     */
    @DeleteMapping("/delete")
    @CacheEvict(value = "questionCache", allEntries = true)
    public Result<String> delete(@RequestBody IdList idList) {
        List<Long> ids = idList.getIds();

        for (Long id: ids) {
            LambdaQueryWrapper<QuestionPaper> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(QuestionPaper::getQuestionId, id);
            List<QuestionPaper> list = questionPaperService.list(lambdaQueryWrapper);
            if (!list.isEmpty()) {
                return Result.error("部分试题无法删除，请确保试卷中不存在待删除的试题");
            }
        }
        questionService.removeByIds(ids);
        return Result.success("删除试题成功");
    }
}
