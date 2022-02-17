package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 用户信息表(User)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:22:11
 */
@Mapper
@Repository
public interface LoginMapper extends BaseMapper<Login> {

    /**
     * 根据userId查询登录信息信息
     * @param userId 用户Id
     * @return Login
     */
    @Select("select * from login where user_id = #{userId}")
    Login findUserInfoById(@Param("userId")long userId);

    /**
     * 根据username获取用户信息
     * @param username 用户名
     * @return login
     */
    @Select("select * from login where user_name = #{username}")
    Login findUserInfoByName(@Param("username")String username);

    /**
     * 更新密码
     * @param username 用户名
     * @param password 密码
     * @return true
     */
    @Update("update login set password = #{password} where user_name=#{username}")
    boolean updatePassword(String username, String password);
}
