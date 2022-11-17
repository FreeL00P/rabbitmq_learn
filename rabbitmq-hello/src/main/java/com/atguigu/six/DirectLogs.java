package com.atguigu.six;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

/**
 * DriectLogs
 *
 * @author fj
 * @date 2022/10/20 20:29
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
            System.out.println("生产者发送消息--> " + message);
        }
    }
}
