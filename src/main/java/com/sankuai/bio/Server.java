/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.bio;

import com.sankuai.bio.socket.SocketProcessor;
import com.sankuai.constants.ColorConstant;
import com.sankuai.utils.PrintUtils;
import com.sankuai.utils.RandomUtil;
import com.sankuai.utils.ScannerUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * 同步IO服务端
 * </p>
 *
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/8 下午9:09 fanyuhao Exp $
 */
public class Server {

    private static final Executor executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(10086);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("接收到一个新的连接: " + socket.getInetAddress().getHostAddress());

            executor.execute(() -> {
                String socketName = socket.getInetAddress().getHostName() + socket.getPort();
                try {
                    while (true) {
                        // 1.先从客户端读消息
                        System.out.println("等待客户端发送的消息..............................");
                        String clientMsg = SocketProcessor.readFromSocket(socket);
                        PrintUtils.printRed("从客户端:%s:读到了如下信息:%s", socketName, clientMsg);

                        // 2.再向客户端写消息
                        System.out.println("有什么话想对客户端说呢，输入完成后请回车结束！");
                        String msg = ScannerUtils.getMsgFromTerminal();
                        SocketProcessor.write2Socket(socket, msg);
                    }
                } catch (Exception e) {
                    System.out.printf("服务端异常了，异常如下:%s", e.getLocalizedMessage());
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
}
