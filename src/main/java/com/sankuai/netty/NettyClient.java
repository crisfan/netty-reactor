/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.sankuai.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :NettyClient.java v1.0 2020/1/17 下午7:17 fanyuhao Exp $
 */
public class NettyClient {

    public static void main(String[] args) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 10086))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println("aa");
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
