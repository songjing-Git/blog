package com.threeman.common.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/26 17:38
 */
public class JucConfig {

    public static ThreadPoolExecutor getThreadPoolExecutor(String username){
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat(username).build();
        return new ThreadPoolExecutor(
                6,
                13,
                2,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), build,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
