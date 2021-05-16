/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.nio.utils;

import java.util.Scanner;

/**
 * <p>
 *  Scanner
 * </p>
 *
 * @author fanyuhao
 * @version :ScannerUtils.java v1.0 2021/5/16 12:17 下午 fanyuhao Exp $
 */
public class ScannerUtils {

    /**
     * 从键盘输入点东西
     * @return
     */
    public static String getMsgFromTerminal() {
        System.out.println("请输入信息，并以回车结束！");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
