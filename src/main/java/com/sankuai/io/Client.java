/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.io;

import com.sankuai.io.socket.SocketProcessor;
import com.sankuai.utils.ScannerUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>
 *  io client
 * </p>
 * @author fanyuhao
 * @version :Client.java v1.0 2019/12/9 下午5:17 fanyuhao Exp $
 */
public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10086);
        socket.connect(address, 1000);

        System.out.println("create new connection");
        OutputStream ops = null;
        try {
            ops = socket.getOutputStream();

            while (true) {
                String msg = ScannerUtils.getMsgFromTerminal();
                SocketProcessor.write2Socket(socket, msg);

                String serverMsg = SocketProcessor.readFromSocket(socket);
                System.out.println("从服务端读到了如下信息:" + serverMsg);
            }
        } catch (IOException e) {
            System.out.println("IO exception");
        } finally {
            if (ops != null) {
                ops.close();
            }
        }
    }
}
