package com.threeman.common.algorithm.sort;

import java.util.*;

/**桶排序
 * @author songjing
 * @version 1.0
 * @date 2022/4/8 10:15
 */
public class BucketSort {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(17,22,13,41,25,65,48,69,78,53);
        Bucket(list);

    }

    public static void Bucket(List<Integer> list){
        //找出最大值与最小值
        int max = list.get(0), min = list.get(0);
        for (Integer a : list) {
            if (a>max){
                max=a;
            }
            if (min>a){
                min=a;
            }
        }
        //确定桶的数量，随意
        int bucketNum=(max-min)/list.size()+1;
        //创建一个bucket
        List<LinkedList<Integer>> bucketList=new ArrayList<>(bucketNum);
        for (int i=0;i<bucketNum;i++){
            bucketList.add(new LinkedList<>());
        }
        System.out.println(bucketList.size());

        for (int i = 0; i < list.size(); i++) {
            int index= (list.get(i)-min)/list.size();
            bucketList.get(index).add(list.get(i));
        }
        //对每个桶内部进行排序
        for(int i=0;i<bucketList.size();i++){
            // 使用Collections.sort，其底层实现基于归并排序或归并排序的优化版本
            Collections.sort(bucketList.get(i));
        }
        // 最后合并所有的桶
        int k = 0;
        for (LinkedList<Integer> b : bucketList) {
            if (b != null) {
                for (int i = 0; i < b.size(); i++) {
                    list.set(k++,b.get(i));
                }
            }
        }
        System.out.println(list);
    }

}
