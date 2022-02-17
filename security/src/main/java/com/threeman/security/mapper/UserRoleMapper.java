package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.UserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户-角色表(UserRole)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:46:03
 */
@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据userId获取角色列表
     * @param userId
     * @return
     */
    @Select("select * from user_role where user_id=#{userId}")
    List<UserRole> getUserRoleInfosByUserId(@Param("userId")long userId);

    @Insert("insert into user_role(user_id,role_id) value(#{userId},#{roleId})")
    int insertUserRole(long userId,long roleId);

    @Delete("delete from user_role where user_id=#{userId}")
    int deleteUserRole(long userId);
}
