/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.bio;

import com.sankuai.bio.socket.SocketProcessor;
import com.sankuai.utils.ScannerUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>
 *  同步IO客户端
 * </p>
 * @author fanyuhao
 * @version :Client.java v1.0 2019/12/9 下午5:17 fanyuhao Exp $
 */
public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10086);

        // block
        socket.connect(address, 5000);

        System.out.println("与服务端建立连接成功");
        try {
            while (true) {

                // 1.先向服务端写消息
                System.out.println("有什么话想对服务端说呢，输入完成后请回车结束！");
                String msg2Server = ScannerUtils.getMsgFromTerminal();
                System.out.printf("客户端向服务端发送如下信息:%s", msg2Server);
                SocketProcessor.write2Socket(socket, msg2Server);

                // 2.从服务端读消息
                System.out.println("等待客户端发送的消息");
                String serverMsg = SocketProcessor.readFromSocket(socket);
                System.out.printf("从服务端读到了如下信息:%s", serverMsg);
            }
        } catch (Exception e) {
            System.out.printf("服务端异常:%s", e.getLocalizedMessage());
        } finally {
            socket.close();
        }
    }
}
