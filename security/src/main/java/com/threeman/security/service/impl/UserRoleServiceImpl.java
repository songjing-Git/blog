package com.threeman.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.security.entity.UserRole;
import com.threeman.security.mapper.UserRoleMapper;
import com.threeman.security.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户-角色表(UserRole)表服务实现类
 *
 * @author songjing
 * @since 2021-10-29 11:46:02
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
