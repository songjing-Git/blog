package com.threeman.servicecore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.mapper.UserMapper;
import com.threeman.servicecore.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author songjing
 * @since 2022-02-16 10:46:18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserInfo(String username) {
        return  userMapper.getUserInfo(username);
    }


}
