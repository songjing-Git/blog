package com.threeman.common.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/1/13 15:42
 */
@Slf4j(topic="c.thread")
public class JUC {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new Thread("Thread"){
            @SneakyThrows
            @Override
            public void run() {
                int o=1;
                log.info("running");
            }
        }.start();

        Runnable running = () -> log.info("running");
        new Thread(running,"Runnable").start();

        Thread.sleep(200);
        log.info("running");

        Callable<Object> callable = () -> {
            log.info("running");
            return "run";
        };
        FutureTask<Object> futureTask = new FutureTask<>(callable);
        new Thread(futureTask,"Callable").start();
        Object o = futureTask.get();
        log.info("o:{}",o);

    }
}
