package com.threeman.common.algorithm;

import com.threeman.common.exception.CreateException;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/3/16 15:03
 */
public class Queue {

    //长度
    private int size;

    //队列
    private Object[] queue;

    //头指针
    private int front;

    //尾指针
    private int rear;

    //默认长度
    private static final int DEFAULT_SIZE = 10;

    public Queue(){
        this.size=DEFAULT_SIZE;
    }

    public Queue(int size){
        this.size=size;
    }

    public boolean isFull(){
        return (rear+1)%size==front;
    }

    public boolean isEmpty(){
        return rear==front;
    }

    public void add(Object ob){
        if (isFull()){
            throw new CreateException("队满");
        }
        queue[rear++]=ob;
    }

    public Object get(){
        if (isEmpty()){
            throw new CreateException("队空");
        }
        Object ob= queue[front];
        queue[front]=null;
        front++;
        return ob;
    }



}
