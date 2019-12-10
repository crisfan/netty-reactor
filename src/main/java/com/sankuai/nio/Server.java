/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 *  nio server
 * </p>
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/9 下午11:26 fanyuhao Exp $
 */
public class Server {

//    public static void main(String[] args) throws IOException {
//        // 1.创建监听连接的 channel
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        serverSocketChannel.
//
//        // 2.将 server channel 注册到 selector 上，并设置监听事件为监听连接
//        Selector selector = Selector.open();
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//        while (selector.select() > 0){
//            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//
//            while (iterator.hasNext()){
//                SelectionKey key = iterator.next();
//
//                // 如果有新连接，则创建与客户端交互的socket，并设置监听事件为可读或可写
//                if(key.isAcceptable()){
//                    SocketChannel channel = serverSocketChannel.accept();
//                    channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
//                }
//
//                // 如果可写
//                if(key.isWritable()){
//
//                }
//
//                // 如果可读，从 channel 中读取数据到 buffer 中
//                if(key.isReadable()){
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//                    SocketChannel channel = (SocketChannel) key.channel();
//                    while (channel.read(byteBuffer) != -1){
//                        String msg = byteBuffer.toString();
//                        System.out.println(msg);
//                    }
//
//                    // 移除监听项:
//                    iterator.remove();
//                }
//            }
//
//        }
//    }
}
