package com.atguigu.four;

import com.atguigu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ConfirmMessage
 * 发布确认模式
 * @author fj
 * @date 2022/10/20 14:36
 * 1.单个确认
 * 2.批量确认
 * 3.异步确认
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT=1000;
    public static void main(String[] args) throws Exception {
        //单个确认
        publishMessageIndividually();
        //批量确认
        publishMessageBatch();
        //异步确认
        publishMessageAsync();
    }
    //单个
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT;i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息发送后马上进行确认
            boolean flag     = channel.waitForConfirms();
            //if (flag) System.out.println("消息发送成功");
        }
        long end= System.currentTimeMillis();
        System.out.println("单个确认"+"发布"+MESSAGE_COUNT+"条消息耗时" + (end-begin)+"ms");
    }
    //批量
    public static void publishMessageBatch() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息长度
        int batchSize=100;
        for (int i = 0; i < MESSAGE_COUNT;i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //100消息发送后马上进行确认

            if ((i +1)%batchSize==0)    channel.waitForConfirms();
        }
        long end= System.currentTimeMillis();
        System.out.println("批量发布"+"发布"+MESSAGE_COUNT+"条消息耗时" + (end-begin)+"ms");
    }
    //异步
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表从适用于高并发的情况下
         * 1 将序号与消息进行关联
         * 2 可以通过序号删除消息
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms=new ConcurrentSkipListMap<>();
        //开始时间
        long begin = System.currentTimeMillis();
        //消息确认成功回调函数
        ConfirmCallback ackCallback=(deliveryTag,multiple)->{
            //判断是否是批量
            if (multiple){
                //删除已经确认的消息
                ConcurrentNavigableMap<Long, String> confirms = outstandingConfirms.headMap(deliveryTag);
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息:"+deliveryTag);
        };
        //消息确认失败回调函数
        /**
         * 参数 1 消息的标记
         * 参数 2 是否为批量确认
         */
        ConfirmCallback nackCallback=(deliveryTag,multiple)->{
            //打印未确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是"+message+"序号："+message);
        };
        //准备消息的监听器 监听哪些消息成功了 哪些失败了
        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback,nackCallback);
        for (int i = 0; i < MESSAGE_COUNT;i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //记录下所有要发布的消息总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        long end= System.currentTimeMillis();
        System.out.println("异步确认"+"发布"+MESSAGE_COUNT+"条消息耗时" + (end-begin)+"ms");

    }
}
