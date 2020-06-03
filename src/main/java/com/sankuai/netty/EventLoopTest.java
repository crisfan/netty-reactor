/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.sankuai.netty;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :EventLoopTest.java v1.0 2020/4/22 下午10:09 fanyuhao Exp $
 */
public class EventLoopTest {

    public static void main(String[] args) {

        NioServerSocketChannel nioServerSocketChannel = new NioServerSocketChannel();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("60 seconds later");
            }
        };
        nioServerSocketChannel.eventLoop().schedule(runnable, 60, TimeUnit.SECONDS);

    }
}
