/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.io;

import com.sankuai.io.socket.SocketProcessor;
import com.sankuai.utils.ScannerUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * io server
 * </p>
 *
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/8 下午9:09 fanyuhao Exp $
 */
public class Server {

    private static Executor executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(10086);

        while (true) {
            System.out.println("waiting for new connection");
            Socket socket = serverSocket.accept();
            System.out.println("got new connection: " + socket.getInetAddress().getHostAddress());

            executor.execute(() -> {
                while (true) {
                    InputStream ins = null;
                    try {
                        ins = socket.getInputStream();
                        String clientMsg = SocketProcessor.readFromSocket(socket);
                        System.out.println("从客户端读到了如下信息:" + clientMsg);

                        String msg = ScannerUtils.getMsgFromTerminal();
                        System.out.println("从客户端写如下信息:" + msg);
                        SocketProcessor.write2Socket(socket, msg);
                    } catch (Exception e) {
                        System.out.println("服务端异常");
                    } finally {
                        try {
                            if (ins != null) {
                                ins.close();
                            }
                        } catch (Exception e) {
                            System.out.println("close ins error");
                        }
                    }
                }
            });
        }
    }
}
