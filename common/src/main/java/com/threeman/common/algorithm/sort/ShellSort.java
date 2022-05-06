package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 希尔排序
 *
 * @author songjing
 * @version 1.0
 * @date 2022/4/1 17:53
 */
public class ShellSort {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(77, 22, 78, 13, 41, 25, 65, 48, 69, 53, 17, 21);
        System.out.println(Shell(list));
    }

    public static List<Integer> Shell(List<Integer> list) {
        //设置希尔排序增量，这里设置为size/2取模
        int step = list.size() / 2;
        int temp;
        while (step > 0) {
            for (int i = step; i < list.size(); ++i) {
                temp = list.get(i);
                int j = i - step;
                while (j >= 0 && temp < list.get(j)) {
                    list.set(j + step, list.get(j));
                    j -= step;
                }

                list.set(j + step, temp);

            }
            step /= 2;
        }
        return list;
    }
}
