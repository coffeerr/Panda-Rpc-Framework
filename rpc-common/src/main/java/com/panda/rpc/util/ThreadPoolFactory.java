package com.panda.rpc.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/25 10:43 上午
 */

public class ThreadPoolFactory {
    private static final int CORE_POOL_SIZE = 100;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private ThreadPoolFactory() {
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, boolean daemon) {
        BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES
                , runnables, threadFactory);
    }

    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(true).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }
}
