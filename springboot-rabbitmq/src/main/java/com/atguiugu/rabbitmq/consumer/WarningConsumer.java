package com.atguiugu.rabbitmq.consumer;

import com.atguiugu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * WarningConsumer
 *
 * @author fj
 * @date 2022/10/23 10:52
 */
@Component
@Slf4j
public class WarningConsumer {
    //接收报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMessage(Message message){
        String msg = new String(message.getBody());
        log.info("发现不可路由消息{}",msg);
    }
}
