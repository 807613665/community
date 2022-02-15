package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.CommentDTO;
import com.lchcommunity.community.dto.ResultDTO;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.mapper.CommentMapper;
import com.lchcommunity.community.model.Comment;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //@RequestBody 将JSON数据自动转换为类
    //@ResponseBody 将return的数据转换为JSON数据
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);

        return ResultDTO.okOf();
    }
}
