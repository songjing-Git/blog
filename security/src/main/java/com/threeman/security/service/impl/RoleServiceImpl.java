package com.threeman.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.security.entity.Role;
import com.threeman.security.mapper.RoleMapper;
import com.threeman.security.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色表(Role)表服务实现类
 *
 * @author songjing
 * @since 2022-03-01 16:27:19
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Autowired
    RoleMapper roleMapper;

    /**
     * 查询所有角色信息
     *
     * @return List<Role>
     */
    @Override
    public List<Role> queryRoleInfos() {
        return roleMapper.queryRoleInfos();
    }

    /**
     * 根据roleName查询角色信息
     *
     * @param roleName 角色名称
     * @return Role
     */
    @Override
    public Role queryRoleInfoByRoleName(String roleName) {
        return roleMapper.queryRoleInfoByRoleName(roleName);
    }

    @Override
    public List<Role> queryRoleInfosByRoleNames(ArrayList<String> roleNames) {
        return roleMapper.queryRoleInfosByRoleNames(roleNames);
    }
}
