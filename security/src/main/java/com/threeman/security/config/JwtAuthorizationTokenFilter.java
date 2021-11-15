package com.threeman.security.config;

import com.auth0.jwt.interfaces.Claim;
import com.three.common.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/12 9:49
 */
public class JwtAuthorizationTokenFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader("token");
        logger.info("=====tokenHeader:"+tokenHeader);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        logger.info("======doFilterInternal");
        chain.doFilter(request, response);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Map<String, Claim> payload = JwtUtil.getPayload(token);
        String username = payload.get("username").asString();
        if (username != null) {
            Claim authority = payload.get("authority");
            String s = authority.asString();
            logger.info("====s:"+s);
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(s);
            return new UsernamePasswordAuthenticationToken(username, simpleGrantedAuthority);
        }
        return null;
    }
}
