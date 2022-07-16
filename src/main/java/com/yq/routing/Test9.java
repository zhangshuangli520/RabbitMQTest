package com.yq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
/**
 * RabbitMQ工作模式四：路由模式，即生产者绑定交换机（direct），交换机通过路由键绑定队列。
 * 一个队列可以绑定多个路由键，生产者通过路由键发送消息到对应的队列。
 * 多个消费者创建各自的队列绑定
 * 交换机：交换机direct(散型交换机)
 */
public class Test9 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        channel.exchangeDeclare("direct_log", BuiltinExchangeType.DIRECT);

        while (true) {
            System.out.println("请输入消息:");
            String message = new Scanner(System.in).nextLine();
            System.out.println("输入路由键：");
            String key = new Scanner(System.in).nextLine();
            channel.basicPublish("direct_log", key, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息已发送");
        }
    }
}
