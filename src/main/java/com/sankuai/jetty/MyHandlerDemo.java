/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai.jetty;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    public static void main(String[] args) {
        System.out.println("aaa");
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
}
