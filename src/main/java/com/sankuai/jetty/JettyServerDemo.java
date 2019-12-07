/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.jetty;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :JettyServerDemo.java v1.0 2019/12/7 下午11:35 qhong Exp $
 */
public class JettyServerDemo {
    public static void main(String[] args) throws Exception {
        // worker threads
        int w = 4;
        // acceptor threads
        int a = 1;
        // selector threads
        int s = 2;
        QueuedThreadPool workers = new QueuedThreadPool(w);
        Server server = new Server(workers);

        Executor executors = Executors.newFixedThreadPool(a + s);
        ServerConnector connector = new NetworkTrafficServerConnector(server, executors, null, null, a, s,
                new HttpConnectionFactory());
        connector.setHost("127.0.0.1");
        connector.setPort(7777);

        server.addConnector(connector);

        ContextHandler btcHandler = new ContextHandler("/btc");
        btcHandler.setHandler(new MyHandlerDemo("world"));

        ContextHandler ethHandler = new ContextHandler("/eth");
        ethHandler.setHandler(new MyHandlerDemo("kitty"));

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.addHandler(btcHandler);
        handlers.addHandler(ethHandler);

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
