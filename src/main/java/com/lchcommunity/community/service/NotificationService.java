package com.lchcommunity.community.service;

import com.lchcommunity.community.dto.NotificationDTO;
import com.lchcommunity.community.dto.PaginationDTO;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.enums.NotificationStatusEnum;
import com.lchcommunity.community.enums.NotificationTypeEnum;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import com.lchcommunity.community.mapper.NotificationMapper;
import com.lchcommunity.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    public PaginationDTO<NotificationDTO> list(Long id, Integer page, Integer size) {
        //查询通知的总数 计算页码
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        Integer questionCount = (int) notificationMapper.countByExample(notificationExample);
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(questionCount, page, size);
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getPageSum())
            page = paginationDTO.getPageSum();

        Integer offset = (page - 1) * size;
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        //根据页码查询具体通知
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");//在sql语句的最后面增加排序  倒序
        List<Notification> list = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        for (Notification notification : list) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }
        //将问题列表放入DTO中
        paginationDTO.setData(notificationDTOList);

        return paginationDTO;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    public Long unreadCount(Long userId){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

}
