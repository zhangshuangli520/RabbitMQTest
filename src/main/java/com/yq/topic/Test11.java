package com.yq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
/**
 * RabbitMQ工作模式五：主题模式，即生产者绑定交换机（topic），交换机通过路由键绑定队列。
 * 和路由模式相似，不同的是一：使用的交换机不同，二：路由键可以用“#”，“*”来模糊匹配
 * 一个队列可以绑定多个路由键，生产者通过路由键发送消息到对应的队列。
 * 多个消费者创建各自的队列绑定
 * 交换机：交换机topic(主题交换机)
 */
public class Test11 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        // 定义交换机
        channel.exchangeDeclare("Topic_Logs", BuiltinExchangeType.TOPIC);
        while (true) {
            System.out.println("输入消息：");
            String mes = new Scanner(System.in).nextLine();
            System.out.println("输入路由键：");
            String key = new Scanner(System.in).nextLine();
            channel.basicPublish("Topic_Logs", key, null, mes.getBytes(StandardCharsets.UTF_8));
        }
    }
}
