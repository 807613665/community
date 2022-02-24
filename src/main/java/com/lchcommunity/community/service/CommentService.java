package com.lchcommunity.community.service;

import com.lchcommunity.community.dto.CommentDTO;
import com.lchcommunity.community.enums.CommentTypeEnum;
import com.lchcommunity.community.enums.NotificationStatusEnum;
import com.lchcommunity.community.enums.NotificationTypeEnum;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import com.lchcommunity.community.mapper.*;
import com.lchcommunity.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentExtMapper commentExtMapper;
    @Autowired
    NotificationMapper notificationMapper;
    //此注释作为默认值应用于声明类及其子类的所有方法。
    @Transactional//事务 将方法中的数据集操作作为一个事务，要么都成功，要么都失败
    public void insert(Comment comment, User commentator) {
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

            insertNotification(comment, dbquestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION.getType(),commentator.getName(),dbquestion.getTitle(), dbquestion.getId());
        }else{
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbcomment==null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);

            //回复问题
            Question dbquestion = questionMapper.selectByPrimaryKey(dbcomment.getParentId());
            if(dbquestion==null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            //更新二级评论数量
            dbcomment.setCommentCount(1);
            commentExtMapper.incCommentcout(dbcomment);

            insertNotification(comment,dbcomment.getCommentator(),NotificationTypeEnum.REPLY_COMMENT.getType(), commentator.getName(), dbquestion.getTitle(), dbquestion.getId());
        }
        comment.setCommentCount(0);
        commentMapper.insert(comment);
    }

    private void insertNotification(Comment comment, Long receiverId, int type, String notifierName, String outerTitle,Long outerId) {
        //回复自己的评论不需要加通知
        if(receiverId == comment.getCommentator())
            return;
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiverId);
        notification.setOuterid(outerId);
        notification.setType(type);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        //获取问题中对应ParentId的所有评论
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");//在sql语句的最后面增加排序  倒序
        List<Comment> comments = commentMapper.selectByExample(commentExample);


        if(comments.size()==0)
            return new ArrayList<>();
        //不为空将评论人写入到set中
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        ArrayList<Long> userIds = new ArrayList<>(commentators);//set转为list

        //获取对应ID的用户信息
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //将用户信息存到map中
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将comment转成CommentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            //将对象的属性注入到另一个对象中
            BeanUtils.copyProperties(comment, commentDTO);
            //设置User
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }
}
