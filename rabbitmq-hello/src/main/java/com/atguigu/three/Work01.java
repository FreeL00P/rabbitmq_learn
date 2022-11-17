package com.atguigu.three;

import com.atguigu.utils.RabbitMQUtils;
import com.atguigu.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * Work01
 * 消息手动应答
 * @author fj
 * @date 2022/10/20 11:04
 */
public class Work01 {
    //队列名称
    public static final String TASK_QUEUE = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1 正在等待接收消息处理时间较短");
        DeliverCallback deliverCallback=(consumerTag,message)->{
            //休眠
            SleepUtils.sleep(1);
            System.out.println("接收到消息message = " + new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 参数 1 消息的标记 tag
             * 参数 2  是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //int prefetchCount=1;//0 公平分发 1 不公平分发
        int  prefetchCount=2;//预取值
        channel.basicQos(prefetchCount);
        //采用手动应答
        channel.basicConsume(TASK_QUEUE,false,deliverCallback,(consumerTag->{
            System.out.println("消费者取消消费接口回调逻辑" + consumerTag);
        }));

    }
}
