package com.yq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ工作模式一：简单模式，即一个生产者一个消费者
 * 交换机：默认交换机direct(直连交换机)
 */
public class Test1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        //  建立链接
        Connection con = factory.newConnection();
        // 在连接中建立一个信道
        Channel channel = con.createChannel();
        // 定义一个队列，如果队列不存在就会新建，如果已经存在相当于空操作
        /**
         * 第一个参数：队列名
         * 第二个参数：是否持久队列，当服务器重启时队列是否仍然存在
         * 第三个参数：独占队列，只能被一个客户端使用
         * 第四个参数：是否自动删除，没有客户端使用该队列时自动删除
         * 第五个参数：其他可配置参数
         */
        channel.queueDeclare("HelloWorld", false, false, false, null);
        channel.basicPublish("","HelloWorld", null, ("hello world" + System.currentTimeMillis()).getBytes());
        System.out.println("消息已经发送");
        channel.close();
    }
}
