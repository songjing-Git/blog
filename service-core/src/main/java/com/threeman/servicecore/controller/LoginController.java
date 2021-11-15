package com.threeman.servicecore.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 11:00
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    public String test(){
        return "登录成功";
    }
}
