package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 角色表(Role)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:41:20
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据roleId获取角色信息
     * @param roleId
     * @return
     */
    @Select("select * from role where role_id = #{roleId}")
    Role getRoleInfoByRoleId(@Param("roleId")long roleId);
}
