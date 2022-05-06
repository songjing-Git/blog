package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 基数排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/11 10:50
 */
public class RadixSort {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17, 22, 13, 41, 25, 65, 48, 69, 78, 53);
        radixSort(list, 2);
        System.out.println(list);
    }

    public static void radixSort(List<Integer> list, int d) {
        int k = 0;
        //位标记 个位n=1 10位n=10
        int n = 1;
        //类似计数排序的桶  按0-9分类
        int[][] temp = new int[10][list.size()];
        //统计桶中的个数
        int[] order = new int[10];
        //循环 由低位到高位
        while (n / 10 <= d) {
            for (int i = 0; i < list.size(); i++) {
                //选出在0-9哪个桶
                int lsd = ((list.get(i) / n) % 10);
                temp[lsd][order[lsd]] = list.get(i);
                //计数加1
                order[lsd]++;
            }
            //循环取出
            for (int i = 0; i < 10; i++) {
                if (order[i] != 0) {
                    for (int j = 0; j < order[i]; j++) {
                        list.set(k, temp[i][j]);
                        k++;
                    }
                }
                order[i] = 0;
            }
            //向上一位
            n *= 10;
            k = 0;
        }
    }
}
