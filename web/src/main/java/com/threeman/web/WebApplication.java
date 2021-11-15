package com.threeman.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 宋京
 */
@ComponentScan("com.threeman.*")
@ComponentScan("com.three.common")
@MapperScan("com.threeman.*")
@ConfigurationPropertiesScan("com.three.common")
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
