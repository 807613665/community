package com.lchcommunity.community.service;

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
    
    public List<QuestionDTO> getAllQuestion(){
        List<Question> list = questionMapper.selectAllQuestion();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : list) {
            User user = userMapper.selectId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
