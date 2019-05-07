package com.exp.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * HTTP客户端工厂，提供的HTTP客户端为{@link HttpClient}的实例，使用
 * {@link MultiThreadedHttpConnectionManager}管理HTTP连接。
 */
public class HttpClientProvider {

    /**
     * HTTP连接管理器，用于维护每个HTTP客户端的连接数、连接超时等配置。
     */
    private static MultiThreadedHttpConnectionManager cm;

    static {
        cm = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams cmp = cm.getParams();
        // （建立socket）连接超时
        cmp.setConnectionTimeout(1000);
        // 响应（socket读取数据）超时
        cmp.setSoTimeout(1000);
        // 管理的连接总数，多个客户端共享
        cmp.setMaxTotalConnections(1);
        // 单个客户主机可用连接最大数
        cmp.setDefaultMaxConnectionsPerHost(1);
        // 每次发送数据前检查连接是否被（服务端）断开
        cmp.setStaleCheckingEnabled(true);
    }

    /**
     * 工厂方法，生成{@link HttpClient}实例，其连接特性由指定
     * {@link MultiThreadedHttpConnectionManager}维护。
     * @return  {@link HttpClient}实例
     */
    public static HttpClient provide() {
        HttpClientParams cp = new HttpClientParams();
        // 连接池（返回空闲可用连接）超时
        cp.setConnectionManagerTimeout(1000L);
        return new HttpClient(cp, cm);
    }
}
