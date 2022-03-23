package com.threeman.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 15:42
 */
@Slf4j(topic="c.thread")
public class JUC {
    public static void main(String[] args) {
        try {
            Thread t1 = new Thread(() -> {
                try {
                    Thread.sleep(100000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "t1");
            log.info("t1.state:{}",t1.getState());
            t1.start();
            log.info("t1.state:{}",t1.getState());
            Thread.sleep(1000);
            log.info("t1.state:{}",t1.getState());
        }catch (Exception e){
            e.getMessage();
        }
    }
}
