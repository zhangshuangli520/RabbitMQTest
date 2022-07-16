package com.yq.test.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Test1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection con = factory.newConnection();
        Channel channel = con.createChannel();

        channel.queueDeclare("logs_direct", false, false, false,null);
        System.out.println("请输入消息：");
        String mes = new Scanner(System.in).nextLine();
        channel.basicPublish("", "logs_direct", null , mes.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息已经发送");
    }
}
