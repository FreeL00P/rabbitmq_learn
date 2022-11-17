package com.atguigu.seven;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * ReceiveLogsTopic01
 * 主题交换机
 * @author fj
 * @date 2022/10/20 20:56
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare("Q1",false,false,false,null);
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");

        System.out.println("ReceiveLogsTopic01 等待接收消息 ");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("message = " +new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("Q1",true,deliverCallback,consumerTag->{});
    }
}
