/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :NettyServer.java v1.0 2019/12/24 下午9:16 fanyuhao Exp $
 */
public class NettyServer {

    public static void main(String[] args) throws Exception{

        // 设置并绑定Reactor线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            // 1.创建服务端启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);

            // 3.设置并绑定服务端channel
            serverBootstrap.channel(NioServerSocketChannel.class);

            // 4.设置handler
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(3));
                    socketChannel.pipeline().addLast(new CustomServerHandler());
                }
            });

            ChannelFuture future = serverBootstrap.bind(10086).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }
}
