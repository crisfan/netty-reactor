/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.test;

import com.google.gson.JsonElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author fanyuhao
 * @version :HttpClient.java v1.0 2021/5/10 5:25 下午 fanyuhao Exp $
 */
public class HttpClient {

    private static final int HTTP_MAX_CONNECTION   = 1024;
    private static final int DEFAULT_MAX_PER_ROUTE = 128;

    /** 从连接池获取连接的timeout */
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;

    /** 客户端和服务器建立连接的timeout */
    private static final int CONNECTION_TIMEOUT = 5000;

    /** 客户端从服务器读取数据的timeout */
    private static final int SOCKET_TIMEOUT = 5000;

    private static final int IO_THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    /** 异步客户端 */
    private static volatile CloseableHttpAsyncClient httpAsyncClient;

    public static CloseableHttpAsyncClient getInstance() {
        if (httpAsyncClient == null) {
            synchronized (HttpClient.class) {
                if (httpAsyncClient == null) {
                    httpAsyncClient = create();
                }
            }
        }
        return httpAsyncClient;
    }


    private static CloseableHttpAsyncClient create() {
        PoolingNHttpClientConnectionManager pcm = null;
        try {
            // 多线程 + SSL
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(IO_THREAD_COUNT).build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

            SSLContext sslContext = createIgnoreVerifySSLContext();
            SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext,
                    SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE).register("https", sslioSessionStrategy).build();

            pcm = new PoolingNHttpClientConnectionManager(ioReactor, registry);
            pcm.setMaxTotal(HTTP_MAX_CONNECTION);
            pcm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        } catch (Exception ignored) {

        }

        // httpAsyncClient配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();

        CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setConnectionManager(pcm)
                .setConnectionReuseStrategy((response, context) -> true)
                .setKeepAliveStrategy((response, context) -> 60 * 1000L)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        client.start();
        return client;
    }

    private static SSLContext createIgnoreVerifySSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                //do nothing
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                //do nothing
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { trustManager }, null);
        return sslContext;
    }

    public static void main(String[] args) {
        CloseableHttpAsyncClient client = getInstance();

        Callback callback = new Callback();

        Map<String, String> map = new HashMap<>();
        map.put("domain", "com.sankuai.sjstopenplatform.push");
        map.put("ip", "10.178.12.113");
        map.put("date", "2021051013");
        map.put("minute", "48");

        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "_lxsdk_cuid=1780c746cd65e-07b91a8de5e0cb-6618227d-13c680-1780c746cd7c8; _lxsdk=1780c746cd65e-07b91a8de5e0cb-6618227d-13c680-1780c746cd7c8; ssoid=eAGFzr1Kw1AYgGEOiAYnyeSYsc1gzvly_j4nY0Nx9GcQXKTnb9QbcGjdHHQQFMxkoVRQULIVXMRr8BLU5gbEQUERcXZ_eXkistAfHpOkfqs_zjOIJEVgWuBy4gANM1z2ZMg59aiDxTQPhvWUM5751ScSt7e92bJ-zx9AmRedDkBXF0IUCJrKripVyYEJhSVN3q_HD1XWIvDvWP-AVubWnqvHr7NsfXI__TzJDkk6P7ux2dl3Po5fRldNPXw9upleDprbUXM3WJxJ-pOLdus3PiXRH6wiS1oqaThyaViOzmoIGikFLwS3RknYZZJpBCWYQkZ3Ept7Km1QyqSaO6sM89wBFYp5qbkJ37Z4ZWI**eAEFwYERADAEBLCVeHoY52ntP0KTuYBgsm7Gs-lIkhuetQeEN-TJdFubUzVWdcoYAIyu9QFH8hFn; yun_portal_ssoid=eAGFzr1Kw1AYgGEOiAYnyeSYsc1gzvly_j4nY0Nx9GcQXKTnb9QbcGjdHHQQFMxkoVRQULIVXMRr8BLU5gbEQUERcXZ_eXkistAfHpOkfqs_zjOIJEVgWuBy4gANM1z2ZMg59aiDxTQPhvWUM5751ScSt7e92bJ-zx9AmRedDkBXF0IUCJrKripVyYEJhSVN3q_HD1XWIvDvWP-AVubWnqvHr7NsfXI__TzJDkk6P7ux2dl3Po5fRldNPXw9upleDprbUXM3WJxJ-pOLdus3PiXRH6wiS1oqaThyaViOzmoIGikFLwS3RknYZZJpBCWYQkZ3Ept7Km1QyqSaO6sM89wBFYp5qbkJ37Z4ZWI**eAEFwYERADAEBLCVeHoY52ntP0KTuYBgsm7Gs-lIkhuetQeEN-TJdFubUzVWdcoYAIyu9QFH8hFn; _lx_utm=utm_source%3Dxm; s_m_id_3299326472=\"AwMAAAA5AgAAAAEAAAMgAAAALOz7pWQXnObtlJc5nOhXqQ4586lr0chGE0tSSVX5DIWk/uFblvnkBcXvISAuAAAAIyr3Cr+2VQ7bA1qpcyQxDJ3jlZKqPvjyLbzcyo3ZCxq1d6Jf\"; logan_session_token=wie681dtuphscul3k728; _lxsdk_s=17968258664-b12-dd7-f14%7C%7C93");

        get("https://raptor.mws.sankuai.com/cat/r/stack/threadDump", map, header
                , callback, 3000);


    }

    public static class Callback implements FutureCallback<HttpResponse>{

        @Override
        public void completed(HttpResponse response) {
            Map<String, Integer> d = new HashMap<>();
            try {
                String json = EntityUtils.toString(response.getEntity(), "utf-8");
                JsonElement element = GsonUtils.parse(json);
                JsonElement data = element.getAsJsonObject().get("data");
                JsonElement threads = data.getAsJsonObject().get("threads");

                Iterator<JsonElement> iterator = threads.getAsJsonArray().iterator();

                while (iterator.hasNext()){
                    JsonElement next = iterator.next();
                    JsonElement name = next.getAsJsonObject().get("name");
                    String asString = name.getAsString();
                    String threadName = asString.split("-")[0];

                    if(d.get(threadName) != null){
                        Integer count = d.get(threadName);
                        d.put(threadName, count + 1);
                    }else {
                        d.put(threadName, 1);
                    }
                }

                Integer sum = 0;
                for (Integer i: d.values()) {
                    sum += i;
                }
                System.out.println(sum);
                System.out.println(GsonUtils.toJson(d));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Exception e) {
            System.out.println("aa");
        }

        @Override
        public void cancelled() {
            System.out.println("aa");
        }
    }


    public static void post(String url,
                            Map<String, String> queries,
                            String body,
                            Map<String, String> headers,
                            String type,
                            FutureCallback<HttpResponse> invocationCallback,
                            int timeout
    ){
        // factory requestBuilder
        RequestBuilder requestBuilder = RequestBuilder.create("POST").setUri(url);

        // set queries
        if (CollectionUtils.isNotEmpty(queries.keySet())) {
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                requestBuilder = requestBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        // set headers
        if (CollectionUtils.isNotEmpty(headers.keySet())) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder = requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // set body
        ContentType contentType = ContentType.create(type, "UTF-8");
        HttpEntity entity = EntityBuilder.create().setText(body).setContentType(contentType).build();
        requestBuilder = requestBuilder.setEntity(entity);

        // set timeout
        if (timeout > 0) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            requestBuilder = requestBuilder.setConfig(requestConfig);
        }

        // factory request
        HttpUriRequest request = requestBuilder.build();

        // invoke
        httpAsyncClient.execute(request, invocationCallback);
    }


    public static void get(String url,
                           Map<String, String> queries,
                           Map<String, String> headers,
                           FutureCallback<HttpResponse> invocationCallback,
                           int timeout){
        // factory requestBuilder
        RequestBuilder requestBuilder = RequestBuilder.create("GET").setUri(url);

        // set queries
        if (CollectionUtils.isNotEmpty(queries.keySet())) {
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                requestBuilder = requestBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        // set headers
        if (CollectionUtils.isNotEmpty(headers.keySet())) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder = requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // set timeout
        if (timeout > 0) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        }

        // factory request
        HttpUriRequest request = requestBuilder.build();

        // invoke
        httpAsyncClient.execute(request, invocationCallback);
    }

}
