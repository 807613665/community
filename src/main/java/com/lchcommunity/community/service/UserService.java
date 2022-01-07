package com.lchcommunity.community.service;

import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void updateOrInsert(User user) {
        User dbuser = userMapper.selectByAccountId(user.getAccountId());
        if(dbuser!=null){
            //更新信息
            dbuser.setName(user.getName());
            dbuser.setAvatarUrl(user.getAvatarUrl());
            dbuser.setToken(user.getToken());
            dbuser.setGmtModified(System.currentTimeMillis());
            userMapper.update(dbuser);
        }else {
            //插入新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
    }
}
