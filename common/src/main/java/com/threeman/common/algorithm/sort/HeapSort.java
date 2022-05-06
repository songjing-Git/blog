package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 堆排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/2 17:58
 */
public class HeapSort {


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17, 22, 13, 41, 25, 65, 48, 69, 78, 53);
        System.out.println(Heap(list));
    }

    //堆排序算法
    public static List<Integer> Heap(List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            //构建大根堆
            maxHeap(list, 0, i);
            //交换
            swap(list, 0, i);
        }
        return list;
    }


    /**
     * 大根堆
     *
     * @param list  数组
     * @param start
     * @param end
     */
    private static void maxHeap(List<Integer> list, int start, int end) {
        //i=0时构建完毕 ，退出
        if (start == end) {
            return;
        }

        //父节点
        int parent = start;
        //左子节点
        int childLeft = start * 2 + 1;
        //右子节点
        int childRight = childLeft + 1;

        //如果左子节点<新加入节点
        if (childLeft <= end) {
            //加入构建
            maxHeap(list, childLeft, end);
            //判断左子节点是否大于父节点
            if (list.get(childLeft) > list.get(parent)) {
                //交换
                swap(list, parent, childLeft);
            }
        }
        if (childRight <= end) {
            maxHeap(list, childRight, end);

            if (list.get(childRight) > list.get(parent)) {
                swap(list, parent, childRight);
            }
        }
    }

    //交换
    private static void swap(List<Integer> list, int a, int b) {
        int temp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, temp);
    }
}
