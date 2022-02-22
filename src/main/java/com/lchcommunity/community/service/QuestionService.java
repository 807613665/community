package com.lchcommunity.community.service;

import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import com.lchcommunity.community.mapper.QuestionExtMapper;
import com.lchcommunity.community.mapper.QuestionMapper;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.QuestionExample;
import com.lchcommunity.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;

    //将Question、User、pageList  组装到一个类中
    public PaginationDTO getQuestion(Integer page, Integer size) {

        Integer questionCount = (int) questionMapper.countByExample(new QuestionExample());
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionCount, page, size);
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getPageSum())
            page = paginationDTO.getPageSum();

        Integer offset = (page - 1) * size;
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");//在sql语句的最后面增加排序  倒序
        List<Question> list = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        for (Question question : list) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将问题列表放入DTO中
        paginationDTO.setQuestionDTOList(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO getQuestion(Long id, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        Integer questionCount = (int) questionMapper.countByExample(questionExample);
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionCount, page, size);
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getPageSum())
            page = paginationDTO.getPageSum();

        Integer offset = (page - 1) * size;
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(id);
        example.setOrderByClause("gmt_create desc");//在sql语句的最后面增加排序  倒序
        List<Question> list = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        for (Question question : list) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将问题列表放入DTO中
        paginationDTO.setQuestionDTOList(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getQuestionById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void updateOrInsert(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            Question record = new Question();
            record.setGmtModified(System.currentTimeMillis());
            record.setTitle(question.getTitle());
            record.setTag(question.getTag());
            record.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(record, example);
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {

        //正则表达式对应语句拼接
        String tag = questionDTO.getTag().replace(',','|');

        //写入到Question中
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(tag);
        List<Question> questionList = questionExtMapper.selectRelated(question);

        //List<Question>-->List<QuestionDTO> 使用注入BeanUtils.copyProperties();
        List<QuestionDTO> questionDTOList = questionList.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            return dto;
        }).collect(Collectors.toList());

        return questionDTOList;
    }
}
