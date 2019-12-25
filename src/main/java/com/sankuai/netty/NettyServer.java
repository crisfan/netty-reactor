/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :NettyServer.java v1.0 2019/12/24 下午9:16 fanyuhao Exp $
 */
public class NettyServer {

    public static void main(String[] args) {
        try{
            // 1.创建服务端启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 2.设置并绑定Reactor线程池
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            serverBootstrap.group(bossGroup, workGroup);

            // 3.设置并绑定服务端channel
            serverBootstrap.channel(NioServerSocketChannel.class);

            // 4.
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new CustomServerHandler());
                }
            });

            serverBootstrap.bind(10086).sync();

        }catch (Exception e){

        }finally {

        }
    }
}
