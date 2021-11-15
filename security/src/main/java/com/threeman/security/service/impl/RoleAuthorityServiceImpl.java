package com.threeman.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.security.entity.RoleAuthority;
import com.threeman.security.mapper.RoleAuthorityMapper;
import com.threeman.security.service.RoleAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色权限表(RoleAuthority)表服务实现类
 *
 * @author songjing
 * @since 2021-10-29 11:46:21
 */
@Service
@Slf4j
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {

}
