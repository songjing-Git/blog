package com.threeman.security.config;

import com.threeman.common.result.Result;
import com.threeman.common.result.ResultEnum;
import com.threeman.common.utils.JwtUtil;
import com.threeman.security.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/21 10:09
 */
@Component
public class MySuccessHandle implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> strings = new ArrayList<>();
        for (GrantedAuthority authority:authorities){
            strings.add(authority.getAuthority());
        }
        User user  =(User) authentication.getPrincipal();
        String jwtToken = JwtUtil.getJwtToken(user.getUserId().toString(),user.getUsername(),user.getPassword(), strings);
        response.setHeader("token",jwtToken);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(new Result<>(ResultEnum.DEFAULT_SUCCESS).toString());
    }
}
