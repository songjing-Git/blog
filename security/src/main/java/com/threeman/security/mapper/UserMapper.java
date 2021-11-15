package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 用户信息表(User)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:22:11
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据userId查询user信息
     * @param userId
     * @return
     */
    @Select("select * from user where user_id = #{userId}")
    User findUserInfoById(@Param("userId")long userId);

    /**
     * 根据username获取用户信息
     * @param username
     * @return
     */
    @Select("select * from user where user_name = #{username}")
    User findUserInfoByName(@Param("username")String username);
}
