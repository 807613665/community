package com.lchcommunity.community.mapper;

import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(int id);
}