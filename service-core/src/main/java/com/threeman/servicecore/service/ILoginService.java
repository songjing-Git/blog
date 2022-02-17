package com.threeman.servicecore.service;

import com.threeman.security.entity.Login;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 11:04
 */
public interface ILoginService {

    /**
     * 新增用户
     * @param user 用户信息
     * @return
     */
    boolean saveLoginInfo(Login user);

    /**
     * 校验用户
     * @param username 用户名
     * @param email 邮箱
     * @return
     */
    boolean verifiesUser(String username,String email);

    /**
     * 更新密码
     * @param username 用户名
     * @param password 密码
     * @return
     */
    boolean updatePassword(String username,String password);
}
