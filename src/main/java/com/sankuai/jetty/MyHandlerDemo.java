/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.jetty;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.UrlEncoded;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 * @author fanyuhao
 * @version :MyHandlerDemo.java v1.0 2019/12/7 下午10:54 qhong Exp $
 */
public class MyHandlerDemo extends AbstractHandler {
    private String name;

    public MyHandlerDemo(String name){
        this.name = name;
    }

    @Override
    public void handle(String target,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletResponse.setContentType("text/html; charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = httpServletResponse.getWriter();
        out.printf("<h1>hello %s</h1>", name);
        request.setHandled(true);
    }

    public static void main(String[] args) throws Exception{
        String encode = URLEncoder.encode("樊宇豪", "UTF-8");
        System.out.println(encode);

        System.out.println("𠮷".length());
        byte[] bytes = "樊".getBytes("utf-8");
        for (byte t: bytes) {
            System.out.println(t);
        }

        String decode = URLDecoder.decode("樊", "utf-8");
        System.out.println(decode);

    }
}
