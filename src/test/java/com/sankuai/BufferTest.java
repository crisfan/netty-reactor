/**
 * meituan.com Inc.
 * Copyright (c) 2010-2019 All Rights Reserved.
 */

package com.sankuai;

import org.junit.Test;

import java.nio.IntBuffer;

/**
 * <p>
 *  Buffer 测试
 * </p>
 * @author fanyuhao
 * @version :BufferTest.java v1.0 2019/12/16 下午5:17 fanyuhao Exp $
 */
public class BufferTest {

    @Test
    public void testInBufferTest(){
        IntBuffer intBuffer = IntBuffer.allocate(3);
        intBuffer.put(1);
        intBuffer.put(2);
        System.out.println(intBuffer);

        // 切换为读模式
        intBuffer.flip();

        // pos 变为 0
        System.out.println(intBuffer.toString());

        // 从头读
        System.out.println(intBuffer.get());
        System.out.println(intBuffer.toString());
        System.out.println(intBuffer.get());
        System.out.println(intBuffer.toString());

        // 如果没有元素了，继续读会抛错 BufferUnderflowException
        // System.out.println(intBuffer.get());
    }
}
