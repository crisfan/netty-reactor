/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.nio.channel;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * channel处理器
 * </p>
 * @author fanyuhao
 * @version :ReadHandle.java v1.0 2021/5/16 11:36 上午 fanyuhao Exp $
 */
public class ChannelProcessor {

    /**
     * 从channel读数据
     * @param channel
     * @throws IOException
     */
    public static String readFromChannel(SocketChannel channel) throws IOException {
        // 每次只从channel内读4个字节，看看一共读了多少次
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        int time = 0;

        // 注：socketChannel 在非阻塞模式下，如果#read返回0，说明channel中传输的数据已经被读完了
        StringBuilder serverMsg = new StringBuilder();
        while (channel.read(byteBuffer) != 0 && channel.read(byteBuffer) != -1) {
            time += 1;

            // 模式调整读模式，开始读
            byteBuffer.flip();
            Charset charset = StandardCharsets.UTF_8;
            CharBuffer charBuffer = charset.decode(byteBuffer);
            serverMsg.append(charBuffer);
            System.out.println("第" + time + "次读，发送的信息：" + charBuffer);

            // 模式调整为写模式，又可以向byteBuffer写
            byteBuffer.clear();
        }

        return serverMsg.toString();
    }


    public static void write2Channel(SocketChannel channel, String msg) throws IOException {
        if(StringUtil.isNullOrEmpty(msg)) {
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] msgBytes = msg.getBytes();

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
        byteBuffer.clear();
    }
}
