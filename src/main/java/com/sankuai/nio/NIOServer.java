/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import com.sankuai.nio.channel.ChannelProcessor;
import com.sankuai.utils.ScannerUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <p>
 *  nio server (多路复用模式)
 * </p>
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/9 下午11:26 fanyuhao Exp $
 */
public class NIOServer {

    private static ByteBuffer byteBuffer = ByteBuffer.allocate(4);

    public static void main(String[] args) throws IOException {
        // 1.创建监听连接的 channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10086));
        serverSocketChannel.configureBlocking(false);

        // 2.将 server channel 注册到 selector 上，并设置监听事件为监听连接
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 3.监听IO事件并处理
        while (selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();

                try {
                    // 3.1 如果有新连接，则创建与客户端交互的 socket，并设置监听事件为可读或可写
                    if(key.isAcceptable()){
                        SocketChannel channel = serverSocketChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    }

                    // 3.2 如果可读，从 channel 中读取数据到 buffer 中
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel)key.channel();
                        String clientMsg = ChannelProcessor.readFromChannel(channel);
                        System.out.println("从客户端读到了如下信息:" + clientMsg);

                        String msg = ScannerUtils.getMsgFromTerminal();
                        System.out.println("向客户端写如下信息:" + msg);
                        ChannelProcessor.write2Channel(channel, msg);
                    }
                }catch (Exception e){
                    System.out.println("Exception happen in Server" + e);
                }finally {
                    // 注: 已经处理过的事件必须移除，否则会一直保留
                    iterator.remove();
                }
            }
        }
    }
}
