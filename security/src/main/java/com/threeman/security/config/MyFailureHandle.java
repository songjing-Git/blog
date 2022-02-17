package com.threeman.security.config;

import com.threeman.common.result.Result;
import com.threeman.common.result.ResultEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/21 10:11
 */
@Component
public class MyFailureHandle implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(new Result<>(ResultEnum.DEFAULT_FAILED,exception.getMessage()).toString());
    }
}
