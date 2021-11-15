package com.threeman.security.testController;

import com.auth0.jwt.interfaces.Claim;
import com.three.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/9 10:27
 */
@Slf4j
@RestController
public class Controller {

    @GetMapping("/userAdd")
    public String test(){
        return "userAdd";
    }
}
