package com.threeman.security.config;

import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/15 17:48
 */
public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private boolean alwaysRemember;

    @Override
    public void setAlwaysRemember(boolean alwaysRemember) {
        this.alwaysRemember = alwaysRemember;
    }

    public MyPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService,
                                                    PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if (alwaysRemember) {
            return true;
        }
        if (request != null && request.getMethod().equalsIgnoreCase("POST") && request.getContentType() != null &&
                (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE))) {
            // 此时我们之前在账号密码拦截器中向Attribute中放的数据可以再次取出来使用了
            // 如果使用request.getInputStream()获取流会发现流已经关闭会报错
            String parameter1 = super.getParameter();
            logger.info("parameter1:" + parameter1);
            String attribute = request.getAttribute(parameter1).toString();
            logger.info("attribute:" + attribute);
            if (attribute == null) {
                return false;
            }
            if ("true".equals(attribute)) {
                return true;
            }
        }
        //否则调用原本的自我记住功能
        return super.rememberMeRequested(request, parameter);
    }
}
