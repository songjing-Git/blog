package com.threeman.canal;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/5/13 14:42
 */
@Configuration
public class CanalConfig {

    @Bean
    Queue queue(){
        return  new Queue("canal_to_redis");
    }

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange("canal_exchange");
    }

    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with("canal_key");
    }
}
