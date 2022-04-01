package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/1 17:51
 */
public class StraightInsertionSort {


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17,22,13,41,25,65,48,69,78,53);
        System.out.println(StraightInsertion(list, 10));
    }

    public static List<Integer> StraightInsertion(List<Integer> list,int size){
        //这里从1开始，因为第一个元素不需要比较
        for (int i = 1; i < size; i++) {
            //将需要排序的元素提取出来
            int key= list.get(i);
            //与前面已经排好的i-1个元素比较
            int j=i-1;
            //比较
            while (j>=0&&key<=list.get(j)){
                //如果key小于当前的数则换位
                list.set(j+1,list.get(j));
                j--;
            }
            //将key放入正确的位置
            list.set(j+1,key);
        }
        return list;
    }
}
