package com.lchcommunity.community.mapper;

import com.lchcommunity.community.model.Comment;
import com.lchcommunity.community.model.CommentExample;
import com.lchcommunity.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentcout(Comment comment);
}