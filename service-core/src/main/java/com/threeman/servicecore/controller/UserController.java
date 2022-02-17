package com.threeman.servicecore.controller;

import com.threeman.common.exception.CreateException;
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

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 11:16
 */
@RestController
@Slf4j
public class UserController {


    @Autowired
    ILoginService loginService;

    @Autowired
    IUserService userService;

    @PutMapping("/register")
    public boolean userRegister(String username,String password){
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new CreateException("注册时用户信息不能为空");
        }
        Login login = new Login();
        login.setUserName(username);
        login.setPassword(password);
        login.setCreateTime(DateUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        login.setUpdateTime(DateUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        login.setState(true);
        log.info("user:{}",login.toString());
        return loginService.saveLoginInfo(login);
    }

    @GetMapping("/getUserInfo/{username}")
    public User getUserInfo(@PathVariable String username){
       if (username==null){
           throw new CreateException("参数不能为空");
       }
        return userService.getUserInfo(username);
    }

    @PostMapping("/verifiesUser")
    public boolean verifiesUser(@RequestBody User user){
        if (user.getUserName()==null||user.getEmail()==null){
            throw new CreateException("参数不能为空");
        }
        return loginService.verifiesUser(user.getUserName(),user.getEmail());
    }

    @GetMapping("/sendVerifiesCode/{email}")
    public Integer sendVerifiesCode(@PathVariable String email) throws javax.mail.MessagingException {
         String regexEmail = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$ ";
        boolean matches = email.matches(regexEmail);
        if (matches){
            throw new CreateException("邮箱格式不正确");
        }
        Integer code= (int) (Math.random() * 1000000);
        EmailUtil.sendMail(email,code.toString());
        return code;
    }

    @PutMapping("updatePassword")
    public boolean updatePassword(@RequestBody Login login){
        return loginService.updatePassword(login.getUsername(),login.getPassword());
    }
}
