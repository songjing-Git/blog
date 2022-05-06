package com.threeman.servicecore.controller;

import com.threeman.common.exception.CreateException;
import com.threeman.common.result.Result;
import com.threeman.common.result.ResultEnum;
import com.threeman.common.utils.DateUtil;
import com.threeman.common.utils.EmailUtil;
import com.threeman.security.entity.Login;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.service.ILoginService;
import com.threeman.servicecore.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 11:16
 */
@RestController
@Slf4j
public class UserController {
    String regexEmail = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$ ";

    @Autowired
    ILoginService loginService;

    @Autowired
    IUserService userService;

    /**
     * 注册用户
     *
     * @param param 入参 username password email
     * @return
     */
    @PutMapping("/register")
    public Object userRegister(@RequestBody Map<String, Object> param) {
        if (param == null || param.isEmpty()) {
            throw new CreateException("参数不能为空");
        }
        String username = param.get("username").toString();
        String password = param.get("password").toString();
        String email = param.get("email").toString();
        boolean matches = email.matches(regexEmail);
        if (matches) {
            throw new CreateException("邮箱格式不正确");
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new CreateException("用户名或密码不能为空");
        }
        Login login = new Login();
        login.setUserName(username);
        login.setPassword(password);
        login.setCreateTime(DateUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        login.setUpdateTime(DateUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        login.setState(true);
        log.info("user:{}", login.toString());
        boolean b;
        try {
            b = loginService.saveLoginInfo(login, email);
        } catch (Exception ignored) {
            log.info(String.valueOf(ignored));
            return new Result<>(ResultEnum.DEFAULT_FAILED, "用户名已存在");
        }
        return b;
    }

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserInfo/{username}")
    public User getUserInfo(@PathVariable String username) {
        if (username == null) {
            throw new CreateException("参数不能为空");
        }
        return userService.getUserInfo(username);
    }

    /**
     * 校验用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/verifiesUser")
    public boolean verifiesUser(@RequestBody User user) {
        if (user.getUserName() == null || user.getEmail() == null) {
            throw new CreateException("参数不能为空");
        }
        return loginService.verifiesUser(user.getUserName(), user.getEmail());
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱地址
     * @return
     * @throws javax.mail.MessagingException
     */
    @GetMapping("/sendVerifiesCode/{email}")
    public Integer sendVerifiesCode(@PathVariable String email) throws javax.mail.MessagingException {

        boolean matches = email.matches(regexEmail);
        if (matches) {
            throw new CreateException("邮箱格式不正确");
        }
        Integer code = (int) (Math.random() * 1000000);
        EmailUtil.sendMail(email, code.toString());
        return code;
    }

    /**
     * 修改登录密码
     *
     * @param login 登录用户名 密码
     * @return
     */
    @PutMapping("updatePassword")
    public boolean updatePassword(@RequestBody Login login) {
        return loginService.updatePassword(login.getUsername(), login.getPassword());
    }

    @PutMapping("updateUserInfo")
    public boolean updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
}
