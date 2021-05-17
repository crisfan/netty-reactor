/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.io.socket;

import java.io.*;
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
     * 从socket读数据
     * @param socket
     * @throws IOException
     */
    public static String readFromSocket(Socket socket) throws IOException {
        InputStream ins = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        return br.readLine();
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
