package com.yq.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient {
    public long call(int n) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.39");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection con= factory.newConnection();
        Channel channel = con.createChannel();
        String queue = channel.queueDeclare().getQueue();
        String id  = UUID.randomUUID().toString();

        AMQP.BasicProperties prop = new AMQP.BasicProperties.Builder().replyTo(queue).correlationId(id).build();
        channel.basicPublish("", "rpc_queue", prop, String.valueOf(n).getBytes(StandardCharsets.UTF_8));
        // 一些其他业务操作.....
        ArrayBlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(10);
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody());
                long l = Long.valueOf(message);
                blockingQueue.offer(l);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicConsume(queue, true, deliverCallback, cancelCallback);
        Long take = blockingQueue.take();
        return take;
    }
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        System.out.println("输入求第几个斐波那契数");
        int n = new Scanner(System.in).nextInt();
        RPCClient client = new RPCClient();
        long r = client.call(n);
        System.out.println(r);
    }
}
