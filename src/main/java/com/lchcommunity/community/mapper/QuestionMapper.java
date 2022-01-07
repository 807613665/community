package com.lchcommunity.community.mapper;

import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //插入新的问题
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag});")
    void insert(Question question);

    //查询
    @Select("select * from question limit #{offset},#{size}")
    List<Question> selectQuestion(@Param("offset") Integer offset, @Param("size") Integer size);

    //查询Creator
    @Select("select * from question where creator=#{id} limit #{offset},#{size}")
    List<Question> selectQuestionByCreator(@Param("id") Integer id,@Param("offset") Integer offset, @Param("size") Integer size);

    //查询总数
    @Select("select count(1) from QUESTION;")
    Integer count();

    @Select("select count(1) from QUESTION where creator=#{id};")
    Integer countByCreator(@Param("id") Integer id);

    @Select("select * from QUESTION where id=#{id};")
    Question getQuestionById(@Param("id") Integer id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}
