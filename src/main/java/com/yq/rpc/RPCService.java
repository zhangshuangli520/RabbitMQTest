package com.yq.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RPCService {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();

        channel.queueDeclare("rpc_queue", false, false, false, null);
        channel.queuePurge("rpc_queue");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String massage = new String(delivery.getBody());
                // 得到消息的整数n,用来求第n个斐波那契数
                int n = Integer.parseInt(massage);
                long f = f(n);
                // 得到队列名
                String queue = delivery.getProperties().getReplyTo();
                // 得到关联id
                String id = delivery.getProperties().getCorrelationId();

                AMQP.BasicProperties prop = new AMQP.BasicProperties.Builder().correlationId(id).build();
                channel.basicPublish("", queue, prop, ("" + f).getBytes(StandardCharsets.UTF_8));
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
                
            }
        };
        channel.basicConsume("rpc_queue", true, deliverCallback, cancelCallback);
    }

    public static long f(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return f(n-2) + f(n-1);
    }
}
