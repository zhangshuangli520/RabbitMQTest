package com.yq.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class YqThreadPool {
    //核心线程数
    private static int corePoolSize = 3;
    //最大线程数
    private static int maximumPoolSize = 5;
    //线程空闲时间
    private static int keepAliveTime = 30;
    //最大队列数
    private static int workQueue = 10;
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private AtomicInteger at = new AtomicInteger(1000);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "yq-thread-" + at.getAndIncrement());
        }
    };
    public static ThreadPoolExecutor newPoolExecutor() {
        System.out.println("corePoolSize:" + corePoolSize);
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,workQueue,threadFactory);
        return threadPoolExecutor;
    }
}
