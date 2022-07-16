package com.yq.workqueue.taskqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ工作模式二：工作队列模式，即一个生产者多个消费者，多个消费者轮询，类似负载均衡
 * 交换机：默认交换机direct(直连交换机)
 * 并且做了消息持久化，保证消息不丢失，通过参数：MessageProperties.PERSISTENT_TEXT_PLAIN实现
 * 同时如果一个消费者繁忙消息会自动发到另一个服务器进行消费，通过关闭自动确认，并且消费者设置channel.basicQos(1);一次只接受一个消息
 */
public class Test3 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        channel.queueDeclare("taskQueue", true, false, false,null);
        while (true) {
            System.out.println("请输入： ");
            String s = new Scanner(System.in).nextLine();
            channel.basicPublish("","taskQueue", MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息已发送");
        }
    }
}
