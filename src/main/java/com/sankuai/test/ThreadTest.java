/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *
 * </p>
 *
 * @author fanyuhao
 * @version :ThreadTest.java v1.0 2021/5/12 8:54 下午 fanyuhao Exp $
 */
public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread thread = Thread.currentThread();
                    System.out.println(thread.getState());

                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.unlock();
            }
        };

        Thread t = new Thread(r, "test");
        t.start();

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                lock.lock();
                try {
                    System.out.println(thread.getState());

                    Thread.sleep(10000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(r2, "test2");

        t2.start();
        t2.join();
    }

}
