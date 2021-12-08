package com.threeman.servicecore.controller;

import com.threeman.security.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 11:00
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/login")
    public String test(){
        return "登录成功";
    }


    @GetMapping("/userAdd")
    @Transactional
    public Object userAdd(){
        return "ok";
    }
}
