/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.io.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

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
        char[] msg = new char[4];
        InputStream ins = socket.getInputStream();

        InputStreamReader reader = new InputStreamReader(ins);

        int time = 0;
        StringBuilder remoteMsg = new StringBuilder();
        while (true) {
            int read = reader.read(msg);
            if(read == 0 || read == -1){
                break;
            }

            time += 1;

            remoteMsg.append(msg);
            System.out.println("第" + time + "次读，发送的信息：" + Arrays.toString(msg));
        }

        return remoteMsg.toString();
    }


    public static void write2Channel(SocketChannel channel, String msg) throws IOException {

    }

}
