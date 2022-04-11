package com.threeman.common.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/8 14:49
 */
public class CountSort {


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17,22,13,41,41,25,65,48,69,78,53);
        sort(list);
    }

    public static void sort(List<Integer> list){
        List<Integer> result = new ArrayList<>(list.size());
        int max=list.get(0),min=list.get(0);
        for (Integer integer : list) {
            if (integer>max){
                max=integer;
            }
            if (integer<min){
                min=integer;
            }
        }
        System.out.println(min);
        int k=max-min+1;
        System.out.println(k);
        int [] temp=new int[k];
        for (int i = 0; i < list.size()-1; i++) {
            temp[list.get(i)-min]+=1;
        }
        for (int i = 0; i < temp.length; i++) {
            int integer = temp[i];
            while (integer!=0){
                result.add(i+min);
                integer--;
            }
        }
        System.out.println(result);
    }
}
