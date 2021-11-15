package com.threeman.security.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/12 11:04
 */
@Slf4j
@Component
public class NoPasswordEncoder  implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return true;
    }
}
