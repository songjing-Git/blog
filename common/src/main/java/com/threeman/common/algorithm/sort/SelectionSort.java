package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 选择排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/2 17:39
 */
public class SelectionSort {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(77, 22, 78, 13, 41, 25, 65, 48, 69, 53, 17, 21);
        System.out.println(Selection(list));
    }

    public static List<Integer> Selection(List<Integer> list) {
        int length = list.size();
        int index = 0;
        int temp = 0;
        //遍历 与最小值交换位置
        for (int i = 0; i <= length - 1; i++) {
            //遍历 找出最小值
            for (int j = i + 1; j <= length - 1; j++) {
                System.out.println("list.get(j)" + list.get(j));
                System.out.println("list.get(j)" + list.get(i));
                //如果后面的数比当前找出的最小值要小，就设置最小值的下标
                if (list.get(j) < list.get(index)) {
                    index = j;
                    System.out.println("if" + index);
                }
            }
            System.out.println("for" + index);
            //找到index的值交换位置
            temp = list.get(i);
            list.set(i, list.get(index));
            list.set(index, temp);
        }

        return list;
    }
}
