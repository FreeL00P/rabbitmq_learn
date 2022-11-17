package com.atguigu.three;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Task01
 * 消息在手动应答时不丢失，放到队列中重新消费
 * @author fj
 * @date 2022/10/20 10:55
 */
public class Task01 {
    //队列名称
    public static final String TASK_QUEUE = "ack_queue";

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
        boolean durable=true;//开启队列持久化
        channel.queueDeclare(TASK_QUEUE,durable,false,false,null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //设置生产者发送消息为持久化消息
            channel.basicPublish("",TASK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息："+message);
        }
    }
}
