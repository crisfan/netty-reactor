/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import com.sankuai.nio.channel.ChannelProcessor;
import com.sankuai.utils.ScannerUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <p>
 * NIO下的客户端
 * </p>
 *
 * @author fanyuhao
 * @version :NIOClient.java v1.0 2019/12/9 下午11:27 fanyuhao Exp $
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        // 1.创建客户端socketChannel
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false); // 一定要开启非阻塞，否则还是读写还是阻塞模式
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10086);

        // 2.创建多路复用使用的selector
        Selector selector = Selector.open(); // 创建一个Selector，监听channel状态

        // 3.建立连接
        boolean connect = channel.connect(address);
        if (connect) {
            channel.register(selector, SelectionKey.OP_READ); // 连接建立成功，监听读写
        } else {
            channel.register(selector, SelectionKey.OP_CONNECT);
        }

        // 4.监听IO事件并处理
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                try {
                    if (key.isConnectable()) {
                        channel.finishConnect();
                        // 注：取消监听连接就绪（否则selector会不断提醒连接就绪）
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT | SelectionKey.OP_READ);

                        key.attach(new StringBuilder());
                        System.out.println("有什么话想对服务端说呢，输入完成后请回车结束！");
                        String msg = ScannerUtils.getMsgFromTerminal();
                        ChannelProcessor.write2Channel(channel, msg);
                    }

                    if (key.isReadable()) {
                        StringBuilder receive = (StringBuilder) key.attachment();
                        String serverMsg = ChannelProcessor.readFromChannel(channel);

                        receive.append(serverMsg);
                        if(serverMsg.charAt(serverMsg.length() - 1) == '\n') {
                            // 已经读完服务端发送的一条信息
                            System.out.printf("服务端给你发送了如下信息:%s，该你回复了哦，输入完成后请回车结束！", receive);
                            receive.delete(0, receive.length());

                            // 向服务端发送信息
                            String msg = ScannerUtils.getMsgFromTerminal();
                            ChannelProcessor.write2Channel(channel, msg);
                        }
                    }
                } catch (Throwable e) {
                    // 异常
                } finally {
                    iterator.remove(); // 需要移除对应监听过的key，否则下次循环该监听的key还在列表里
                }
            }
        }
    }
}
