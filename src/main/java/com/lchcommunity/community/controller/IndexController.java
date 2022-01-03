package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.mapper.QuestionMapper;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.QuestionDTOService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.jws.WebParam;
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
            Model model
    ){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
            for(Cookie i:cookies){
                if(i.getName().equals("token")){
                    User user = userMapper.cheackToken(i.getValue());
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }

        List<QuestionDTO> questionDTOList = questionDTOService.getAllQuestion();
        model.addAttribute("questions",questionDTOList);

        return "index";
    }
}
