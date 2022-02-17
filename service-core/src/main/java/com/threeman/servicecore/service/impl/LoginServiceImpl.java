package com.threeman.servicecore.service.impl;

import com.threeman.common.exception.CreateException;
import com.threeman.security.entity.Login;
import com.threeman.security.entity.UserRole;
import com.threeman.security.mapper.LoginMapper;
import com.threeman.security.mapper.UserRoleMapper;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.mapper.UserMapper;
import com.threeman.servicecore.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 11:05
 */
@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {


    private final int defaultRoleId=10000;

    @Autowired
    LoginMapper loginMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public boolean saveLoginInfo(Login login) {
        int i=0;
        if (loginMapper.insert(login)==1){
            login = loginMapper.findUserInfoByName(login.getUsername());
            List<UserRole> userRoleInfos = userRoleMapper.getUserRoleInfosByUserId(login.getUserId());
            if (!userRoleInfos.isEmpty()){
                userRoleMapper.deleteUserRole(login.getUserId());
            }
            i = userRoleMapper.insertUserRole(login.getUserId(), defaultRoleId);
        }
        return i==1;
    }

    @Override
    public boolean verifiesUser(String username, String email) {
        User userInfo = userMapper.getUserInfo(username);
        if (userInfo==null){
            throw new CreateException("用户名验证失败");
        }
        if (!email.equals(userInfo.getEmail())){
            throw new CreateException("邮箱验证失败");
        }
        return true;
    }

    @Override
    public boolean updatePassword(String username, String password) {
        return loginMapper.updatePassword(username,password);
    }
}
