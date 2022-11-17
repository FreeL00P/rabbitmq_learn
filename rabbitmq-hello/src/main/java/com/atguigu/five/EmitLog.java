package com.atguigu.five;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

/**
 * EmitLog
 * 发消息给交换机
 * @author fj
 * @date 2022/10/20 20:00
 */
public class EmitLog {
    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println("生产者发送消息--> " + message);
        }
    }
}
