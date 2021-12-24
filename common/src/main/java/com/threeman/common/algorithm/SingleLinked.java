package com.threeman.common.algorithm;

import lombok.Data;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/10 16:18
 */
@Data
public class SingleLinked {

    /**
     * 节点类
     */
    @Data
    private static class Node{
        private Object data;

        private Node next;

    }

    private Node head;

    private int size;

    public void add(Node node){
        Node temp=head;
        while (true){
            if (temp.next==null){
                temp.next=node;
                break;
            }else {
                temp=temp.next;
            }
        }
    }


}
