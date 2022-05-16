package com.threeman.servicecore.config.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import com.threeman.servicecore.entity.Support;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/5/13 14:52
 */

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = "canal_to_redis")
    public void  getMsg(String msg){
        log.info("msg:{}",msg);
        Entity<Support> stringObjectMap = JSONObject.parseObject(msg, new TypeReference<Entity<Support>>() {
        });
        log.info("s:{}",stringObjectMap);
    }
}
