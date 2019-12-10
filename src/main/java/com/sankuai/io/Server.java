/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 *  io server
 * </p>
 * @author fanyuhao
 * @version :Server.java v1.0 2019/12/8 下午9:09 fanyuhao Exp $
 */
public class Server {

    private static Executor executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(10086);

        while (true){
            System.out.println("waiting for new connection");
            Socket socket = serverSocket.accept();
            System.out.println("got new connection: " + socket.getInetAddress().getHostAddress());

            executor.execute(() -> {
                InputStream ins = null;
                try{
                    ins = socket.getInputStream();
                    readMsgWithLine(ins);
                }catch (Exception e){
                    System.out.println("read fail");
                }finally {
                    try {
                        if(ins != null){
                            ins.close();
                        }
                    }catch (Exception e){
                        System.out.println("close ins error");
                    }
                }
            });
        }
    }

    /**
     * 从流中读取数据
     * @param ins 输入流
     * @return
     * @throws IOException
     */
    private static void readMsg(InputStream ins) throws IOException {
        int n;
        byte[] msg = new byte[10];
        StringBuilder sb = new StringBuilder();

        while ((n = ins.read(msg)) != -1){
            String copyMsg = new String(msg, 0, n);
            if("end\n".equals(copyMsg)){
                System.out.println(sb.toString());
                sb.delete(0, sb.length());
                continue;
            }
            sb.append(copyMsg);
        }
    }

    private static void readMsgWithLine(InputStream ins) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        String line = null;
        while ((line = br.readLine()) != null){
            if("end".equals(line)){
                System.out.println(sb.toString());
                sb.delete(0, sb.length());
                continue;
            }

            if("quit".equals(line)){
                System.out.println("close connection");
                break;
            }

            sb.append(line);
        }
    }
}
