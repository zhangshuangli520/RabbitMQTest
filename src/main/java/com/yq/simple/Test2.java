package com.yq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 获取链接
        Connection con = factory.newConnection();
        // 建议信道
        Channel channel = con.createChannel();
        // 定义队列
        channel.queueDeclare("HelloWorld",false, false, false, null);

        // 消费数据的回调对象
        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                byte[] body = delivery.getBody();
                String message = new String(body);
                System.out.println("接收到：" + message);
            }
        };
        // 取消消息的回调对象
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        // 消费消息
        channel.basicConsume("HelloWorld", true, callback, cancelCallback);
    }
}
