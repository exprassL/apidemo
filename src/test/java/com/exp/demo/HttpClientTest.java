package com.exp.demo;

import com.exp.common.util.ExecutorServiceProvider;
import com.exp.common.util.HttpClientProvider;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * {@link HttpClientProvider}测试类。
 */
public class HttpClientTest {

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> workQueue =
                new ArrayBlockingQueue<>(1, false);
        ExecutorService es = ExecutorServiceProvider.provide(2, 5, 20,
                TimeUnit.SECONDS, true, workQueue, (r, executor) -> System.out.println("放弃了一个任务"));
        HttpClient c = HttpClientProvider.provide();
        for (int i = 0; i < 2; i++) {
            es.execute(new MyTask(c));
        }
    }

    static class MyTask implements Runnable {

        private HttpClient c;

        MyTask(HttpClient c) {
            this.c = c;
        }

        /**
         * @see Thread#run()
         */
        @Override
        public void run() {
            String url = "http://202.102.221.85:20188/idxs/autoComplete.do" +
                    "?cityId=100&q=xbs&limit=15&timestamp=1556588236750";
            GetMethod get = new GetMethod(url);
            get.setRequestHeader("Accept", "text/html," +
                    "application/xhtmlxml,application/xml;q=0.9," +
                    "image/webp,image/apng,*/*;q=0.8");
            get.setRequestHeader("Accept-Encoding", "gzip, deflate");
            get.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
            get.setRequestHeader("Cache-Control", "no-cache");
            get.setRequestHeader("Connection", "keep-alive");
            // get.setRequestHeader("Cookie", "val");
            get.setRequestHeader("Host", "202.102.221.85:20188");
            get.setRequestHeader("Pragma", "no-cache");
            get.setRequestHeader("Upgrade-Insecure-Requests", "1");
            get.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT " +
                    "10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                    "Gecko) Chrome/69.0.3497.92 Safari/537.36 ");
            try {
                int status = c.executeMethod(get);
                // 此处休眠线程将导致HTTP连接被占用
                Thread.sleep(1000L);
                if (status == 200) {
                    String jsonStr =
                            IOUtils.toString(get.getResponseBodyAsStream(), "utf-8");
                    System.out.println(c);
                    System.out.println(jsonStr);
                }
                // 此处休眠线程时连接已断开
                // TODO 由服务端断开？
                Thread.sleep(1000L);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                get.releaseConnection();
            }
        }
    }
}