package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
     * @param roleId 角色ID
     * @return
     */
    @Select("select * from role where role_id = #{roleId}")
    Role getRoleInfoByRoleId(@Param("roleId")long roleId);

    /**
     * 查询所有角色信息
     * @return List<Role>
     */
    @Select("select * from role ")
    List<Role> queryRoleInfos();

    /**
     * 根据roleName获取角色信息
     * @param roleName 角色名称
     * @return Role
     */
    @Select("select * from role where role_name = #{roleName}")
    Role queryRoleInfoByRoleName(@Param("roleName")String roleName);

    @Select("select * from role where role_name in (#{roleName})")
    List<Role> queryRoleInfosByRoleNames(@Param("roleName") ArrayList<String> roleNames);
}
