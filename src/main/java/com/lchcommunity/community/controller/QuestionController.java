package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionDTOService questionDTOService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model) {
        QuestionDTO question = questionDTOService.getQuestionById(id);
        model.addAttribute("questions", question);
        return "question";
    }
}
