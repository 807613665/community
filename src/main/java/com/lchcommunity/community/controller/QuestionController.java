package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {
        //增加阅读数
        questionService.incView(id);
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("questions", question);
        return "question";
    }
}
