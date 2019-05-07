package com.exp.common.util;

import java.util.concurrent.*;

/**
 * 线程池工厂，提供线程池工具{@link ExecutorService}实例的生成方法。
 */
public class ExecutorServiceProvider {

    /**
     * 线程池工厂方法，接受入参用于初始化并返回线程池。
     *
     * @param corePoolSize      核心线程数，小于0时抛异常
     * @param maxPoolSize       最大线程数，小于/等于0时抛异常
     * @param keepAliveTime     线程空闲允许时间，小于0抛异常
     * @param unit              线程空闲允许时间的单位，见枚举类{@link TimeUnit}
     * @param coreThreadTimeOut 是否允许核心线程超时
     * @param workQueue         阻塞任务的临时存储队列
     * @param handler           线程池已满&线程无空闲&任务队列已满时，新提交任务
     *                          由方法调用方调用此处理器进行处理，处理器内部逻辑
     *                          由调用方定义在该入参中
     * @return 配置好的线程池工具{@link ExecutorService}实例
     */
    public static ExecutorService provide(int corePoolSize, int maxPoolSize,
                                          int keepAliveTime, TimeUnit unit,
                                          boolean coreThreadTimeOut,
                                          BlockingQueue<Runnable> workQueue,
                                          RejectedExecutionHandler handler) {
        ThreadPoolExecutor pool =
                new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                        unit, workQueue, handler);
        pool.allowCoreThreadTimeOut(coreThreadTimeOut);
        return pool;
    }
}
