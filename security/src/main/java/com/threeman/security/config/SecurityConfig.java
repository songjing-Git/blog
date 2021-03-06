package com.threeman.security.config;

import com.threeman.common.exception.CreateException;
import com.threeman.security.entity.Authority;
import com.threeman.security.mapper.AuthorityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/28 20:26
 */
@Slf4j
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthorityMapper authorityMapper;


    @Resource
    DataSource dataSource;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    MySuccessHandle mySuccessHandle;

    @Resource
    MyFailureHandle myFailureHandle;

    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        MyPersistentTokenBasedRememberMeServices rememberMeServices = new MyPersistentTokenBasedRememberMeServices("INTERNAL_SECRET_KEY", new UserDetailsServiceImpl(), persistentTokenRepository());
        // ??????????????????remember-me???rememberMe?????????????????????key?????????
        rememberMeServices.setParameter("remember_value");
        //token?????????7???
        rememberMeServices.setTokenValiditySeconds(3600 * 24 * 7);
        return rememberMeServices;
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        //??????WebSecurityConfigurerAdapter?????????AuthenticationManager????????????????????????AuthenticationManager
        return new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<Authority> authorityInfos = authorityMapper.getAuthorityInfo();
        if (authorityInfos.isEmpty()) {
            throw new CreateException(402, "????????????????????????");
        }
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/favicon.ico").permitAll();
        http.formLogin().loginProcessingUrl("/login")
                .successHandler(mySuccessHandle)
                .failureHandler(myFailureHandle);
        http.rememberMe()
                .rememberMeParameter("remember_value")
                .rememberMeCookieName("isRemember")
                .tokenValiditySeconds(3600)
                .tokenRepository(persistentTokenRepository());
        http.logout().permitAll();
        for (Authority authorityInfo : authorityInfos) {
            http.authorizeRequests().antMatchers(authorityInfo.getAuthorityUrl()).hasAnyAuthority(authorityInfo.getAuthorityCode());
        }
        //http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
        MyAuthenticationFilter myAuthenticationFilter = new MyAuthenticationFilter();
        myAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        myAuthenticationFilter.setAuthenticationSuccessHandler(mySuccessHandle);
        myAuthenticationFilter.setAuthenticationFailureHandler(myFailureHandle);
        myAuthenticationFilter.setRememberMeServices(rememberMeServices());
        http.addFilter(myAuthenticationFilter);
        http.addFilter(new JwtAuthorizationTokenFilter(authenticationManager()));
        http.csrf().disable();
        http.cors();
        http.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                //????????????????????????
                new Header("Access-control-Allow-Origin", "*"),
                //???ajax??????????????????header??????jwt token??????
                new Header("Access-Control-Expose-Headers", "token"))));
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("121.41.59.128");
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
                "HEAD", "CONNECT", "TRACE", "OPTIONS"));
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/index.html", "/static/**", "/favicon.ico")
                .antMatchers("/register", "/insertEsModel", "/verifiesUser", "/sendVerifiesCode/*",
                        "/updatePassword", "/addUserRole", "/findLabels/*", "/insertBlog", "/getBlogInfos/*"
                        , "/excelReader", "/excelReaderByOrderId", "/addBlogComment", "/delBlogComment/*"
                        , "/getBlogInfo/**", "/getBlogInfosByPage/*", "/addBlogView/*", "/blogSupport", "/commentSupport"
                        , "/blogComment", "/findBlogComment/**", "/getCommentSupportCount/*", "/getBlogSupportCount/*"
                )
                // ??? swagger ?????? ?????????????????????????????????
                .antMatchers("/swagger-ui.html", "/doc.html", "/null/**",
                        "/swagger-resources/**",
                        "/images/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/csrf",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-resources/configuration/security",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources");


        web.ignoring()
                .antMatchers(
                        "swagger-ui.html",
                        "**/swagger-ui.html",
                        "/favicon.ico",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.gif",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/**/*.ttf"
                );
    }
}
