package com.exp.demo;

import com.exp.common.util.ExecutorServiceProvider;

import java.util.concurrent.*;

/**
 * {@link ExecutorServiceProvider}测试类。
 */
public class ExecutorServiceTest {

    /**
     * 程序编译&运行的时间节点，线程运行时间与此时间节点取差值，用于比较线程运行
     * 的先后顺序
     */
    private static long origin = System.currentTimeMillis();

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> workQueue =
                new ArrayBlockingQueue<>(50, false);
        ExecutorService es = ExecutorServiceProvider.provide(
                5, 10, 3,
                TimeUnit.SECONDS, true, workQueue,
                (r, executor) -> {
                    /*
                     * 自定义任务处理器的可执行方法的实现
                     */
                    MyTask task = (MyTask) r;
                    String name = Thread.currentThread().getName();
                    System.out.println(System.currentTimeMillis() - origin + "：" + name + "放弃：任务号=" + task.taskId);
                });
        for (int i = 0; i < 65; i++) {
            es.execute(new MyTask(i));
        }
    }

    /**
     * 自定义可执行任务，继承{@link Runnable}接口。
     */
    static class MyTask implements Runnable {
        private int taskId;

        MyTask(int taskId) {
            this.taskId = taskId;
        }

        /**
         * @see Thread#run()
         */
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            /*
             * 将任务执行开始时间减去编译&运行时间，获得一个较小的数字,
             * 记录下来便于判断任务开始顺序
             */
            System.out.println(System.currentTimeMillis() - origin + "：" +
                    name + "开始：任务号=" + taskId);
            try {
                System.out.println(System.currentTimeMillis() - origin + "：" +
                        name + "暂停5s：任务号=" + taskId);
                Thread.sleep(1000L * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() - origin + "：" +
                    name + "结束：任务号=" + taskId);
        }
    }
}