package com.atguiugu.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * MyCallBack
 *
 * @author fj
 * @date 2022/10/22 15:54
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * @param correlationData 保存回调消息的ID以及相关信息
     * @param ack 交换机是否收到消息
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id=correlationData!=null ? correlationData.getId() :"";
        if (ack){
            log.info("交换机成功接收到消息ID-->{}",id);
        }else {
            log.info("交换机还未收到ID-->{} 原因：{}",id,cause);
        }
    }
    //回退
    //消息传递过程中不可达到目的地时将消息返回给生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{}被交换机{}回退，原因：{}路由key:{}",returnedMessage.getMessage(),
                returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }
}
