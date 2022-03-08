package com.threeman.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.security.entity.UserRole;

import java.util.ArrayList;

/**
 * 用户-角色表(UserRole)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:46:01
 */
public interface UserRoleService extends IService<UserRole> {


    boolean insertUserRole(long userId, ArrayList<String> roleName);
}
