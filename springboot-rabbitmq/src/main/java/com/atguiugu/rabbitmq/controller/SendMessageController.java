package com.atguiugu.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.atguiugu.rabbitmq.config.DelayQueueConfig.DELAYED_EXCHANGE_NAME;
import static com.atguiugu.rabbitmq.config.DelayQueueConfig.DELAYED_ROUTING_KEY;

/**
 * SendMessageCOntroller
 *  发送延迟消息
 * @author fj
 * @date 2022/10/21 23:28
 */
@RestController
@RequestMapping("ttl")
@Slf4j
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{}，发送消息给两个TTL队列：{}，", new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的延迟队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的延迟队列"+message);

    }
    //开始发消息 消息&TTl
    @GetMapping("/sendExpirationMsg/{message}/{ttl}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttl){
        log.info("当前时间：{}，发送消息给TTL队列：{} TTL={}", new Date().toString(),message,ttl);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为"+ttl+"ms的延迟队列"+message,msg->{
           //发送消息时 设置延迟时长
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
    }
    //开始发送消息基于插件 消息以及延迟的时间
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime){
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData ->{
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info(" 当 前 时 间 ： {}, 发送一条延迟 {} 毫秒的信息给队列 delayed.queue:{}", new
                Date(),delayTime, message);
    }

}
