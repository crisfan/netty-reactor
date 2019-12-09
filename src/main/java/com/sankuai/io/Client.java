/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10086);
        socket.connect(address, 1000);

        System.out.println("建立连接");
        OutputStream ops = null;
        try {
            while (true){
                System.out.println("请输入:");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                ops = socket.getOutputStream();

                int n;
                char[] msg = new char[10];
                while ((n = br.read(msg)) != -1){
                    String copyMsg = new String(msg, 0, n);
                    ops.write(copyMsg.getBytes());
                }
            }
        }catch (IOException e){
            System.out.println("IO exception");
        }finally {
            if(ops !=null){
                ops.close();
            }
        }

    }
}
