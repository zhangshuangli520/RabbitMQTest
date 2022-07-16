package com.yq.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
/**
 * RabbitMQ工作模式三：发布订阅模式，即一个生产者绑定一个交换机（fanout），多个消费者创建各自的队列绑定交换机
 * 交换机：交换机fanout(散型交换机)
 */
public class Test7 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        channel.exchangeDeclare("logs", "fanout");
        while (true) {
            System.out.println("输入：");
            String msg = new Scanner(System.in).nextLine();
            channel.basicPublish("logs","",null,msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
