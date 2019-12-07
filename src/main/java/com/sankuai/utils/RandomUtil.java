/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.utils;

import java.util.Random;

/**
 * <p>
 *
 * </p>
 *
 * @author fanyuhao
 * @version :RandomUtil.java v1.0 2021/5/30 5:52 下午 fanyuhao Exp $
 */
public class RandomUtil {
    private static Random random = new Random();

    public static int getRandomNum(int max, int min) {
        return random.nextInt(max - min + 1) + min;
    }
}
