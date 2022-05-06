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
public class NoPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        log.info("charSequence" + charSequence);
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        log.info("密码是否匹配:{}", charSequence.equals(s));
        if (charSequence.equals(s)) {
            return true;
        }
        return false;
    }
}
