package com.atguigu.five;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * ReceiveLogs01
 *
 * @author fj
 * @date 2022/10/20 19:44
 */
public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME="logs";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机 type=fanout(扇出)
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列（临时）
        String queueName=channel.queueDeclare().getQueue();

        //绑定交换机与队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("ReceiveLogs01等待接收消息 ");
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println("message = " +new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }

}
