package com.atguigu.four.rabbirmq;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Consumer
 * 消费者 接收队列中的消息
 * @author fj
 * @date 2022/10/19 21:49
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME="hello";
    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("message = " + new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback=consumerTag->{
            System.out.println("消息消费被中断");
        };
        /**
         * 接收消息
         * 参数 1  消费哪个队列
         * 参数 2 消费成功后是否要自动应答
         * 参数 3 消费者未成功消费的一个会调
         * 参数 4  消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
