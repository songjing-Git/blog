package com.threeman.common.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 归并排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/7 17:01
 */
public class MergeSort {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(77, 22, 78, 13, 41, 25, 65, 48, 69, 53, 17, 21);
        merge(list, 0, list.size() - 1, new ArrayList<>(16));

    }

    //归并排序算法
    private static void merge(List<Integer> list, int start, int end, List<Integer> temp) {
        //递归结束条件
        if (start >= end) {
            return;
        }

        //分治 位
        int mid = (start + end) / 2;
        //先递归两两分组
        merge(list, start, mid, temp);
        merge(list, mid + 1, end, temp);
        //分组完毕
        //分组开头的下标
        int f = start;
        //分组后中间数的下标
        int s = mid + 1;
        //临时数组下标
        int t = 0;
        //如果在分组中 开始的数小于中间的数，中间的数小于最后一个数
        while (f <= mid && s <= end) {
            //将较小的数存入临时数组
            if (list.get(f) < list.get(s)) {
                temp.add(t++, list.get(f++));
            } else {
                temp.add(t++, list.get(s++));
            }
        }
        while (f <= mid) {
            temp.add(t++, list.get(f++));
        }

        while (s <= end) {
            temp.add(t++, list.get(s++));
        }

        for (int i = 0, j = start; i < t; i++) {
            list.set(j++, temp.get(i));
        }
        System.out.println(list);
    }
}
