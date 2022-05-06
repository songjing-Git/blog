package com.threeman.security.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/15 14:38
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("请求方式:" + request.getMethod());
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        logger.info("请求格式:" + request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE));
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            String username = null;
            String password = null;
            try {
                Map<String, String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                logger.info("UsernameParameter:" + super.getUsernameParameter());
                username = map.get(super.getUsernameParameter());
                password = map.get(super.getPasswordParameter());
                Object rememberMe = map.get("remember_value");
                if (username == null) {
                    username = "";
                }

                if (password == null) {
                    password = "";
                }

                username = username.trim();
                logger.info("username:" + username);
                request.setAttribute("remember_value", rememberMe);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        username, password);

                // Allow subclasses to set the "details" property
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.attemptAuthentication(request, response);

    }


}
