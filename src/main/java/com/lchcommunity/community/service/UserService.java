package com.lchcommunity.community.service;

import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void updateOrInsert(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> dbusers = userMapper.selectByExample(userExample);
        if(dbusers.size()!=0){
            User dbuser=dbusers.get(0);
            //更新信息
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setAvatarUrl(user.getAvatarUrl());
            newUser.setToken(user.getToken());
            newUser.setGmtModified(System.currentTimeMillis());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbuser.getId());
            userMapper.updateByExampleSelective(newUser, example);
        }else {
            //插入新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
    }
}
