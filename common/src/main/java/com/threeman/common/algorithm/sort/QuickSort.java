package com.threeman.common.algorithm.sort;

import java.util.Arrays;
import java.util.List;

/**快速排序
 * @author songjing
 * @version 1.0
 * @date 2022/4/1 15:05
 */
public class QuickSort {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17,22,13,41,25,65,48,69,78,53);
        System.out.println(Quick(list, 0, 9));
    }

    public static List<Integer> Quick(List<Integer> list,int low,int high){
        if (low<high){
            //找出pivot的位置并赋值
            int getMid=getMid(list,low,high);
            //分治递归
            if (low<getMid-1){
                Quick(list,low,getMid-1);
            }
            if (high>getMid+1){
                Quick(list,getMid+1,high);
            }
        }
        return list;
    }

    public static int getMid(List<Integer> list,int low,int high) {
        //随便取出一个基准值，这里取第一个
        int pivot=list.get(low);
        //先从后往前找，因为可以把找出的值覆盖在基点的位置(基准值已存为临时变量可覆盖)
        while (list.get(high)>=pivot&&low<high){
            //如果大于基准值就往前找
            high--;
        }
        //如果不符合条件，就把小于基准的值放在基准值前面
        list.set(low,list.get(high));
        //再从前往后找
        while (list.get(low)<=pivot&&low<high){
            low++;
        }
        //将大于基准的值放到后面
        list.set(high,list.get(low));
        //将基准值放入正确的位置
        list.set(low,pivot);
        //返回基准值的位置，再根据这个位置进行分治递归
        return low;
    }
}
