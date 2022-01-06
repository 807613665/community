package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionDTOService questionDTOService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {
        //获取该页面获取的问题 分页
        PaginationDTO paginationDTO = questionDTOService.getQuestion(page, size);
        model.addAttribute("paginationDTO", paginationDTO);

        return "index";
    }
}
