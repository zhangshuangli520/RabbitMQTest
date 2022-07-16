package com.yq.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test5 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        channel.queueDeclare("HelloWorld", false, false, false, null);

        DeliverCallback deliverCallback = new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        String message = new String(delivery.getBody());
                        System.out.println("开始处理消息：" + message);
                        System.out.println("消息处理完毕：" + message);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicQos(1);
        channel.basicConsume("HelloWorld", false, deliverCallback, cancelCallback);

    }
}
