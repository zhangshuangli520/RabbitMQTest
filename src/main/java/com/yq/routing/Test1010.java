package com.yq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Test1010 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();

        channel.exchangeDeclare("direct_log", BuiltinExchangeType.DIRECT);
        String queue = channel.queueDeclare().getQueue();
        System.out.println("请输入用逗号隔开的绑定键");
        String s = new Scanner(System.in).nextLine();
        String[] split = s.split(",");
        for (String key : split) {
            channel.queueBind(queue, "direct_log", key);
        }
        DeliverCallback deliverCallback= new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String s1 = new String(delivery.getBody());
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(s1 + "------" + routingKey);
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicConsume(queue,true, deliverCallback, cancelCallback);
    }
}
