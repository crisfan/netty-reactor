/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import org.eclipse.jetty.util.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * <p>
 *  nio server (多路复用模式)
 * </p>
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/9 下午11:26 fanyuhao Exp $
 */
public class Server {

    private static ByteBuffer byteBuffer = ByteBuffer.allocate(4);

    public static void main(String[] args) throws IOException {
        // 1.创建监听连接的 channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10086));
        serverSocketChannel.configureBlocking(false);

        // 2.将 server channel 注册到 selector 上，并设置监听事件为监听连接
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();

                try {
                    // 2.1 如果有新连接，则创建与客户端交互的 socket，并设置监听事件为可读或可写
                    if(key.isAcceptable()){
                        SocketChannel channel = serverSocketChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    }

                    // 2.2 如果可读，从 channel 中读取数据到 buffer 中
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel)key.channel();
                        String msg = readMsg(channel);

                        // 原样写回客户端
                        if(StringUtil.isNotBlank(msg)){
                            writeMsg(channel, msg);
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

    /**
     * 读取客户端信息，并打印
     * @param channel
     * @throws IOException
     */
    private static String readMsg(SocketChannel channel) throws IOException {
        int n;
        StringBuilder copyMsg = new StringBuilder();
        while ((n = channel.read(byteBuffer)) != 0){
            System.out.println("read " + n + " bytes");
            if(n == -1){
                System.out.println("client close the connection");
                break;
            }

            // 转化为读模式
            byteBuffer.flip();
            Charset charset = StandardCharsets.UTF_8;
            CharBuffer charBuffer = charset.decode(byteBuffer);
            copyMsg.append(charBuffer.toString());

            // 转化为写模式
            byteBuffer.clear();
        }

        System.out.println(copyMsg);
        return copyMsg.toString();
    }

    /**
     * 向服务端写信息
     * @param channel
     * @param copyMsg
     * @throws IOException
     */
    private static void writeMsg(SocketChannel channel, String copyMsg) throws IOException{
        byte[] msgBytes = copyMsg.getBytes();

        int start = 0;
        int len = Math.min(byteBuffer.capacity(), msgBytes.length);

        // 如果 byteBuffer 的大小 < 发送信息大小，分批发送
        if(msgBytes.length > byteBuffer.capacity()){
            while (start < msgBytes.length){
                // 切换为写模式
                byteBuffer.clear();
                byteBuffer.put(msgBytes, start, len);

                // 切换为读模式
                byteBuffer.flip();
                channel.write(byteBuffer);

                start += len;
                len = Math.min(byteBuffer.capacity(), msgBytes.length - start);
            }
            return;
        }

        // 如果 byteBuffer 的大小 >= 发送信息大小
        byteBuffer.clear();
        byteBuffer.put(msgBytes, 0, msgBytes.length - 1);

        // 切换读模式
        byteBuffer.flip();
        channel.write(byteBuffer);
    }
}
