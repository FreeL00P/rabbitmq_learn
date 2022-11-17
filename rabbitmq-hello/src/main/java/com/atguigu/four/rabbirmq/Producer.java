package com.atguigu.four.rabbirmq;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * onr
 * 生产者：发消息
 * @author fj
 * @date 2022/10/19 21:16
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME="hello";
    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 生成一个 队列
         * 参数 1 队列名称
         * 参数 2 队列里的消息是否持久化 默认消息存储在内存中
         * 参数 3 该队列是是否只供一个消费者进行消费 是否进行消息共享
         *  true可以多个消费者消费 false 只能一个消费者消费
         * 参数 4 是否自动删除,最后一个消费者离开连接后是否自动删除 true 删除
         * 参数 5 其他参数 (延迟消息等)
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //开始发送消息
        String message="hello world";
        //用信道进行消息发送 目前不用到交换机（exChange）
        /**
         * 发送一个消息
         * 参数 1 发送到哪个交换机 本次不使用
         * 参数 2 路由的key值 本次队列名称
         * 参数 3 其他
         * 参数 4 发送的消息实体  需要转换为二进制
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
