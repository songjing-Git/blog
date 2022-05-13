package com.threeman.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/5/13 14:52
 */

@Slf4j
public class Consumer {

    @RabbitListener(queues = "canal_to_redis")
    public void  getMsg(Message message, Channel channel, String msg){
        log.info("message:{}",message);
        log.info("channel:{}",channel);
        log.info("msg:{}",msg);
        String[]chars=msg.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<chars.length;i++){
            stringBuffer.append((char)Integer.parseInt(chars[i]));
        }
        JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
        System.out.println(jsonObject);
    }
}
