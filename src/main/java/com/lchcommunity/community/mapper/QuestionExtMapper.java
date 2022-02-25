package com.lchcommunity.community.mapper;

import com.lchcommunity.community.dto.QuestionQueryDTO;
import com.lchcommunity.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question question);
    int incCommentcout(Question question);
    List<Question> selectRelated(Question question);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}