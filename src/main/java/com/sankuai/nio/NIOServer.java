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
                        System.out.printf("接收到来自远程服务器的连接：%s", getRemoteAddress(channel));
                        channel.configureBlocking(false);
                        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
                        selectionKey.attach(new StringBuilder());
                    }

                    // 3.2 如果可读，从 channel 中读取数据到 buffer 中
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel)key.channel();
                        StringBuilder receive = (StringBuilder) key.attachment();
                        String clientMsg = ChannelProcessor.readFromChannel(channel);

                        receive.append(clientMsg);
                        if(clientMsg.charAt(clientMsg.length() - 1) == '\n') {
                            // 已经读到客户端最后一条发的信息
                            System.out.printf("从客户端:%s读到了如下信息:%s，要及时回复客户端哦，输入完成后请回车结束！", getRemoteAddress(channel), receive);
                            receive.delete(0, receive.length());

                            // 向客户端发送信息
                            String msg = ScannerUtils.getMsgFromTerminal();
                            ChannelProcessor.write2Channel(channel, msg);
                        }
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

    private static String getRemoteAddress(SocketChannel channel) throws IOException {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
        return remoteAddress.getAddress().getHostAddress() + ":" +  remoteAddress.getPort();
    }
}
