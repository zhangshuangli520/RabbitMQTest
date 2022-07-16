package com.yq.test.workqueue;

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

        channel.queueDeclare("taskQueue", true, false, false, null);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {

                String mes = new String(delivery.getBody());
                System.out.println("收到消息" + mes);
                for (int i = 0; i < mes.length(); i++) {
                    char c = mes.charAt(i);
                    String s1 = String.valueOf(c);
                    if (s1.equals(".")) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.println("消息消费完成");
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicQos(1);
        channel.basicConsume("taskQueue", deliverCallback, cancelCallback);
    }
}
