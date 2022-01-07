package com.lchcommunity.community.mapper;

import com.lchcommunity.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //新建用户
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl});")
    void insert(User user);

    //查询用户的token
    @Select("select * from user where token = #{token}")
    User cheackToken(@Param("token") String token);

    //查询用户
    @Select("select * from user where id = #{id}")
    User selectId(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User selectByAccountId(@Param("accountId") String accountId);

    @Update("update user set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where id=#{id}")
    void update(User user);
}
