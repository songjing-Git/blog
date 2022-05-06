package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.RoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限表(RoleAuthority)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:46:21
 */
@Mapper
@Repository
public interface RoleAuthorityMapper extends BaseMapper<RoleAuthority> {

    /**
     * 根据roleId获取权限列表
     *
     * @param roleId
     * @return
     */
    @Select("select * from role_authority where role_id = #{roleId}")
    List<RoleAuthority> getRoleAuthorityInfosByRoleId(@Param("roleId") long roleId);
}
