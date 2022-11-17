package com.atguigu.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * getChannel
 *
 * @author fj
 * @date 2022/10/19 22:33
 */
public class RabbitMQUtils {
    public static Channel getChannel() {
        //创建工厂
        ConnectionFactory factory=new ConnectionFactory();
        //工厂ip连接rabbitMQ队列
        factory.setHost("127.0.0.1");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        //获取信道
        try {
            return connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
