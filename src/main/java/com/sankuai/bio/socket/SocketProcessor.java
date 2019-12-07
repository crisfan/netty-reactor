/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.bio.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  socket处理器
 * </p>
 *
 * @author fanyuhao
 * @version :SocketProcessor.java v1.0 2021/5/16 3:10 下午 fanyuhao Exp $
 */
public class SocketProcessor {

    /**
     * 从socket读数据，默认读到'\n'结束
     * @param socket
     * @throws IOException
     */
    public static String readFromSocket(Socket socket) throws IOException {
        // 初始化4个字节的缓存来缓存数据
        byte[] buffer = new byte[4];

        InputStream ins = socket.getInputStream();

        StringBuilder sb = new StringBuilder();
        while (true) {
            int size = ins.read(buffer);
            if(size == -1) {
                // 对端已经关闭连接
                throw new RuntimeException("对端已经关闭连接，别自作多情了");
            }

            String msg = new String(buffer, 0, size, StandardCharsets.UTF_8);
            sb.append(msg);
            if(msg.charAt(msg.length() - 1) == '\n') {
                break;
            }
        }

        return sb.toString();
    }


    /**
     * 向socket写数据
     * @param socket
     * @throws IOException
     */
    public static void write2Socket(Socket socket, String msg) throws IOException {
        OutputStream ops = socket.getOutputStream();
        ops.write(msg.getBytes(StandardCharsets.UTF_8));
        ops.flush();
    }

}
