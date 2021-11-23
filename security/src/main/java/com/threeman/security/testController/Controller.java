package com.threeman.security.testController;

import com.threeman.security.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/9 10:27
 */
@Slf4j
@RestController
public class Controller {


    @Autowired
    UserMapper userMapper;

    @GetMapping("/userAdd")
    @Transactional
    public String test(){
        userMapper.findUserInfoById(1);
        userMapper.findUserInfoById(1);
        return "userAdd";
    }
}
