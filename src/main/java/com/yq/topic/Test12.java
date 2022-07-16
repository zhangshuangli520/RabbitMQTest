package com.yq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Test12 {
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
        // 定义队列
        String queue = channel.queueDeclare().getQueue();
        System.out.println("请输入逗号隔开的绑定键");
        String keys = new Scanner(System.in).nextLine();
        String[] split = keys.split(",");
        for (String key:split) {
            channel.queueBind(queue, "Topic_Logs", key);
        }

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String mes = new String(delivery.getBody());
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(routingKey + "----------" + mes);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicConsume(queue, true ,deliverCallback, cancelCallback);
    }
}
