package com.atguiugu.rabbitmq.consumer;

import com.atguiugu.rabbitmq.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * DelayQueueComsumer
 * 基于插件的延迟消息
 * @author fj
 * @date 2022/10/22 11:25
 */
@Component
@Slf4j
public class DelayQueueConsumer {
    //监听消息
    @RabbitListener(queues = DelayQueueConfig.DELAYED_QUEUE_NAME)
    public void receiverDelayQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
    }
}
