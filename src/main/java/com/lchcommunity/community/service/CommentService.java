package com.lchcommunity.community.service;

import com.lchcommunity.community.enums.CommentTypeEnum;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import com.lchcommunity.community.mapper.CommentMapper;
import com.lchcommunity.community.mapper.QuestionExtMapper;
import com.lchcommunity.community.mapper.QuestionMapper;
import com.lchcommunity.community.model.Comment;
import com.lchcommunity.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==CommentTypeEnum.QUESTION.getType()){
            //回复问题
            Question dbquestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(dbquestion==null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            dbquestion.setCommentCount(1);
            questionExtMapper.incCommentcout(dbquestion);
        }else{
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbcomment==null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);

        }
        commentMapper.insert(comment);
    }
}
