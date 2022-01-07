package com.lchcommunity.community.service;

import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.mapper.QuestionMapper;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionDTOService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;

    //将Question、User、pageList  组装到一个类中
    public PaginationDTO getQuestion(Integer page, Integer size) {

        Integer questionCount = questionMapper.count();
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionCount, page, size);
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getPageSum())
            page = paginationDTO.getPageSum();

        Integer offset = (page - 1) * size;
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        List<Question> list = questionMapper.selectQuestion(offset, size);
        for (Question question : list) {
            User user = userMapper.selectId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将问题列表放入DTO中
        paginationDTO.setQuestionDTOList(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO getQuestion(Integer id, Integer page, Integer size) {
        Integer questionCount = questionMapper.countByCreator(id);
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionCount, page, size);
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getPageSum())
            page = paginationDTO.getPageSum();

        Integer offset = (page - 1) * size;
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        List<Question> list = questionMapper.selectQuestionByCreator(id,offset, size);
        for (Question question : list) {
            User user = userMapper.selectId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将问题列表放入DTO中
        paginationDTO.setQuestionDTOList(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getQuestionById(Integer id){
        Question question = questionMapper.getQuestionById(id);
        User user = userMapper.selectId(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }
}
