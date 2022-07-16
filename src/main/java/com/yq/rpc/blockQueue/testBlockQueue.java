package com.yq.rpc.blockQueue;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class testBlockQueue {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);

        new Thread() {
            @Override
            public void run() {
                System.out.println("线程1-请输入消息：");
                String s = new Scanner(System.in).nextLine();
                blockingQueue.offer(s);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                System.out.println("线程2-等待消息：");
                String take = null;
                try {
                    take = blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2-接收消息：" + take);
            }
        }.start();

    }
}
