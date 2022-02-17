package com.threeman.security.config;

import com.threeman.common.result.Result;
import com.threeman.common.result.ResultEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/15 16:19
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(String.valueOf(new Result<>(ResultEnum.DEFAULT_FAILED,"访问失败!")));

    }
}
