/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :NIOClient.java v1.0 2019/12/9 下午11:27 fanyuhao Exp $
 */
public class Client {

    public static void main(String[] args) throws Exception{
        SocketChannel channel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10086);
        channel.connect(address);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n;
            char[] msg = new char[10];

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while ((n = br.read(msg)) != -1){
                String copyMsg = new String(msg, 0, n);
                channel.write(ByteBuffer.wrap(copyMsg.getBytes()));

                /**
                 * 注：socketChannel 在非阻塞模式下，如果#read返回0，说明byteBuffer处于满的状态
                 */
                while (channel.read(byteBuffer) != 0){
                    // 转化为读模式
                    byteBuffer.flip();
                    Charset charset = StandardCharsets.UTF_8;
                    CharBuffer charBuffer = charset.decode(byteBuffer);
                    System.out.println(charBuffer.toString());

                    // 切换为写模式
                    byteBuffer.clear();
                }

                if("quit\n".equals(copyMsg)){
                    System.out.println("close connection");
                    break;
                }
            }
        }catch (IOException e){
            System.out.println("IO exception");
        }finally {
            if(channel != null){
                channel.close();
            }
        }
    }
}
