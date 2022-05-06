package com.threeman.servicecore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.servicecore.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 用户信息表(User)表服务接口
 *
 * @author songjing
 * @since 2022-02-16 10:46:24
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @Select("select * from user where user_name = #{username}")
    User getUserInfo(String username);
}
