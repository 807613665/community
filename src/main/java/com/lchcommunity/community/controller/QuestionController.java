package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.CommentDTO;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.enums.CommentTypeEnum;
import com.lchcommunity.community.service.CommentService;
import com.lchcommunity.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {
        //传递问题列表
        List<CommentDTO> comments = commentService.listByTargetId(id,CommentTypeEnum.QUESTION);
        model.addAttribute("comments", comments);

        //增加阅读数
        questionService.incView(id);
        QuestionDTO question = questionService.getQuestionById(id);

        //相关问题列表
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(question);

        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("questions", question);
        return "question";
    }
}
