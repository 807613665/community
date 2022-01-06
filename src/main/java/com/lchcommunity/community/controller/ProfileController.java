package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionDTOService questionDTOService;

    @GetMapping("/profile/{element}")
    public String profile(@PathVariable("element") String element,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {
        User user = (User)request.getSession().getAttribute("user");
        if (user == null)
            return "redirect:/";

        if ("question".equals(element)) {
            model.addAttribute("element", "question");
            model.addAttribute("elementName", "我的问题");
        }
        if ("reply".equals(element)) {
            model.addAttribute("element", "reply");
            model.addAttribute("elementName", "最新回复");
        }

        //获取该页面获取的问题 分页
        PaginationDTO paginationDTO = questionDTOService.getQuestion(user.getId(),page, size);
        model.addAttribute("paginationDTO", paginationDTO);



        return "profile";
    }
}
