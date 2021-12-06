package com.threeman.security.config;

import com.threeman.common.exception.CreateException;
import com.threeman.security.entity.*;
import com.threeman.security.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/28 20:30
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    AuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(s)){
            throw  new CreateException(202,"用户名不能为空");
        }
        User userInfo = userMapper.findUserInfoByName(s);
        if (userInfo==null){
            throw new CreateException("用户不存在");
        }
        //获取用户userId
        Long userId = userInfo.getUserId();
        //根据userId获取用户角色关联信息
        List<UserRole> userRoleInfos = userRoleMapper.getUserRoleInfosByUserId(userId);
        //用于登录用户角色存储
        List<Role> roles=new ArrayList<>();
        //用于登录用户权限信息存储
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        //遍历对应关系获取权限信息
        for (UserRole userRole : userRoleInfos){
            Role role = roleMapper.getRoleInfoByRoleId(userRole.getRoleId());
            List<RoleAuthority> roleAuthorityInfos = roleAuthorityMapper.getRoleAuthorityInfosByRoleId(userRole.getRoleId());
            for (RoleAuthority roleAuthority: roleAuthorityInfos){
                Long authorityId = roleAuthority.getAuthorityId();
                Authority authorityInfo = authorityMapper.getAuthorityInfoByAuthorityId(authorityId);
                grantedAuthorities.add(new SimpleGrantedAuthority(authorityInfo.getAuthorityName()));
            }
            roles.add(role);
        }
        //将角色存储在User中
        userInfo.setRoles(roles);
        //将权限信息存储在User中
        userInfo.setGrantedAuthorities(grantedAuthorities);
        return userInfo;
    }
}
