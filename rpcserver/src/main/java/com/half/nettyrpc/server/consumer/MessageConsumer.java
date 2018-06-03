package com.half.nettyrpc.server.consumer;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @author wangwei
 * @description  消息消费者，负责
 * @date Create in 2018-06-03 10:53
 **/
public class MessageConsumer {

    private static ExecutorService threadPoolExecutor;

    private static int minThreads=8;

    private static int maxThreads=100;

    public static int getMinThreads() {
        return minThreads;
    }

    public static void setMinThreads(int minThreads) {
        MessageConsumer.minThreads = minThreads;
    }

    public static int getMaxThreads() {
        return maxThreads;
    }

    public static void setMaxThreads(int maxThreads) {
        MessageConsumer.maxThreads = maxThreads;
    }

    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (MessageConsumer.class) {
                if (threadPoolExecutor == null) {

                    threadPoolExecutor = new ThreadPoolExecutor(minThreads, maxThreads, 0, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new DefaultThreadFactory("RPC", true));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
