package com.threeman.servicecore.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

    private final String EXCHANGE_NAME="canal_exchange";
    private final String QUEUE_NAME="canal_to_redis";

    @Bean("canal_exchange")
    public Exchange canalExchange(){
        log.info("exchange");
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("canal_to_redis")
    public Queue queue(){
        log.info("queue");
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding bindingQueueExchange(@Qualifier("canal_to_redis") Queue queue,@Qualifier("canal_exchange") Exchange exchange){
        log.info("binding");
        return BindingBuilder.bind(queue).to(exchange).with("canal_key").noargs();
    }

}
