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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(s)){
            throw  new CreateException(202,"用户名不能为空");
        }
        User userInfo = userMapper.findUserInfoByName(s);
        log.info("userInfo:{}",userInfo);
        if (userInfo==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        Long userId = userInfo.getUserId();
        List<UserRole> userRoleInfos = userRoleMapper.getUserRoleInfosByUserId(userId);
        List<Role> roles=new ArrayList<>();
        userInfo.setRoles(roles);
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        for (UserRole userRole : userRoleInfos){
            Role role = roleMapper.getRoleInfoByRoleId(userRole.getRoleId());
            List<RoleAuthority> roleAuthorityInfos = roleAuthorityMapper.getRoleAuthorityInfosByRoleId(userRole.getRoleId());
            for (RoleAuthority roleAuthority: roleAuthorityInfos){
                Long authorityId = roleAuthority.getAuthorityId();
                log.info("authorityId:{}",authorityId);
                Authority authorityInfo = authorityMapper.getAuthorityInfoByAuthorityId(authorityId);
                grantedAuthorities.add(new SimpleGrantedAuthority(authorityInfo.getAuthorityName()));
            }
            roles.add(role);
        }
        userInfo.setGrantedAuthorities(grantedAuthorities);
        userInfo.setRoles(roles);
        log.info("matches:{}",passwordEncoder.matches("songjing",userInfo.getPassword()));
        log.info("userInfo:{}",userInfo);
            return userInfo;
    }
}
