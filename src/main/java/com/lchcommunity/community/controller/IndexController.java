package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {
        //获取该页面获取的问题 分页
        PaginationDTO paginationDTO = questionService.getQuestion(page, size);
        model.addAttribute("paginationDTO", paginationDTO);

        return "index";
    }
}
