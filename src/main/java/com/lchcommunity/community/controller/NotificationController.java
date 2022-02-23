package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.NotificationDTO;
import com.lchcommunity.community.enums.NotificationTypeEnum;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);
        if(notificationDTO.getType()== NotificationTypeEnum.REPLY_QUESTION.getType()||
                notificationDTO.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }
    }
}
