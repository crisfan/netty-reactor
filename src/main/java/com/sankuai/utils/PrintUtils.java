/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.utils;

import com.sankuai.constants.ColorConstant;

/**
 * <p>
 *  终端打印工具，结合ColorStants
 * </p>
 *
 * @author fanyuhao
 * @version :PrintUtis.java v1.0 2021/5/30 5:56 下午 fanyuhao Exp $
 */
public class PrintUtils {

    public static void printRed(String format, Object... msg) {
        System.out.printf(ColorConstant.ANSI_RED + format + ColorConstant.ANSI_RESET, msg);
    }
}
