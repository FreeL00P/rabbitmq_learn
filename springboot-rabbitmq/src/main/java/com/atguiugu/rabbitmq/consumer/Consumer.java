package com.atguiugu.rabbitmq.consumer;

import com.atguiugu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer
 * 接收消息
 * @author fj
 * @date 2022/10/22 15:36
 */
@Component
@Slf4j
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("接受到队列 {} 消息:{}",ConfirmConfig.CONFIRM_QUEUE_NAME,msg);
    }
}
