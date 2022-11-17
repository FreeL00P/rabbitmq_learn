package com.atguigu.deadLetter;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * deadTask
 * 死信队列 消息分发
 * @author fj
 * @date 2022/10/21 14:24
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Channel channel = RabbitMQUtils.getChannel()) {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            //设置消息的 TTL 时间
            AMQP.BasicProperties properties = new
                    AMQP.BasicProperties().builder().expiration("10000").build();
            //该信息是用作演示队列个数限制
            for (int i = 1; i <11 ; i++) {
                String message="info"+i;
                channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties,
                        message.getBytes());
                System.out.println("生产者发送消息:"+message);
            }
        }
    }
}
