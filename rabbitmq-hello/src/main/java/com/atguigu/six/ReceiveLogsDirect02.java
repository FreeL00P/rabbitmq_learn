package com.atguigu.six;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * ReceiveLogsDirect01
 *
 * @author fj
 * @date 2022/10/20 20:18
 */
public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare("disk",false,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        System.out.println("ReceiveLogsDirect02 等待接收消息 ");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("message = " +new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag->{});
    }
}
