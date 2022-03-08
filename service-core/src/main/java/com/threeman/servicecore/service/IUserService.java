package com.threeman.servicecore.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.servicecore.entity.User;

/**
 * 用户信息表(User)表服务接口
 *
 * @author songjing
 * @since 2022-02-16 10:46:18
 */
public interface IUserService extends IService<User> {


    User getUserInfo(String username);

    boolean updateUserInfo(User user);

}
