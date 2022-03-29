package com.threeman.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 15:42
 */
@Slf4j(topic="c.thread")
public class JUC {

    private static Object ReentrantReadWriteLock;

    public static void main(String[] args) {
        //查看线程数
        try {
            Runtime.getRuntime().availableProcessors();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object o = new Object();
        try {
            o.wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
