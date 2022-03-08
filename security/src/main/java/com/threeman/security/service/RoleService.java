package com.threeman.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.security.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色表(Role)表服务接口
 *
 * @author songjing
 * @since 2022-03-01 16:25:11
 */
public interface RoleService extends IService<Role> {


    List<Role> queryRoleInfos();

    Role queryRoleInfoByRoleName(String roleName);

    List<Role> queryRoleInfosByRoleNames(ArrayList<String> roleNames);
}
