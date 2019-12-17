/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * <p>
 *  非阻塞模式 (非多路复用模式)
 * </p>
 * @author fanyuhao
 * @version :NonBlockServer.java v1.0 2019/12/16 下午7:31 fanyuhao Exp $
 */
public class NonBlockServer {

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10086));
        serverSocketChannel.configureBlocking(false);

        SocketChannel socketChannel;
        while (true){
            socketChannel = serverSocketChannel.accept();
            if(Objects.nonNull(socketChannel)){
                socketChannel.configureBlocking(false);
                System.out.println("new connection");
                break;
            }
            System.out.println("waiting conn");
        }

        while (true){
            ByteBuffer buffer = ByteBuffer.allocate(5);
            if(socketChannel.read(buffer) == -1){
                System.out.println("waiting for data");
                continue;
            }
            System.out.println(buffer);
        }
    }
}
