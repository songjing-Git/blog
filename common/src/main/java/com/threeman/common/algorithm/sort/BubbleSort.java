package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 冒泡排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/1 14:35
 */
public class BubbleSort {


    public static void main(String[] args) {
        //定义一串数组
        List<Integer> list = Arrays.asList(17, 22, 13, 41, 25, 65, 48, 69, 78, 53);
        //调用冒泡排序
        System.out.println(Bubble(list));
    }


    /**
     * 冒泡排序实现
     *
     * @param list
     * @return
     */
    public static List<Integer> Bubble(List<Integer> list) {
        //循环两次，j从0开始,最大下标为list.size()-1
        for (int j = 0; j < list.size() - 1; j++) {
            //list.size()-1-j之后的数是之前已经排好的,所以不用比较，所以比较list.size()-1-j的数
            for (int i = 0; i < list.size() - 1 - j; i++) {
                //相邻两个数比较，将较大的数后移
                if (list.get(i) > list.get(i + 1)) {
                    int temp = list.get(i + 1);
                    list.set(i + 1, list.get(i));
                    list.set(i, temp);
                }
            }
        }
        return list;
    }

}
