package com.threeman.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.common.result.Result;
import com.three.common.result.ResultEnum;
import com.three.common.utils.JwtUtil;
import com.threeman.security.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/9 21:08
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager=authenticationManager;
        super.setFilterProcessesUrl("/login");
    }

    /**
     * 调用loadUserByUsername之前进入的方法
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("进入JwtAuthenticationFilter的attemptAuthentication方法");
        log.info("request.getInputStream():{}",request);
        User userInfo = new ObjectMapper().readValue(request.getInputStream(), User.class);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userInfo.getUsername(),userInfo.getPassword()));
    }

    /**
     * 验证成功后进入方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.info("进入JwtAuthenticationFilter的successfulAuthentication方法");
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        User user  =(User) authResult.getPrincipal();
        String jwtToken = JwtUtil.getJwtToken(user.getUserId().toString(),user.getUsername(),authorities.toString());
        response.setHeader("token",jwtToken);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(new Result<>(ResultEnum.DEFAULT_SUCCESS).toString());
    }

    /**
     * 验证失败进入的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write(new Result<>(ResultEnum.DEFAULT_FAILED,failed.getMessage()).toString());
    }
}
