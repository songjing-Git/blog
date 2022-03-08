package com.threeman.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.exception.CreateException;
import com.threeman.security.entity.Role;
import com.threeman.security.entity.UserRole;
import com.threeman.security.mapper.UserRoleMapper;
import com.threeman.security.service.RoleService;
import com.threeman.security.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户-角色表(UserRole)表服务实现类
 *
 * @author songjing
 * @since 2021-10-29 11:46:02
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {



    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public boolean insertUserRole(long userId, ArrayList<String> roleNames) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.in("role_name",roleNames);
        List<Role> roles = roleService.list(roleQueryWrapper);
        if (roles==null){
            throw new CreateException("查不到角色信息");
        }
        int i =0;
        for (Role role:roles){
            i=userRoleMapper.insertUserRole(userId, role.getRoleId());
        }
        return i>0;
    }
}
