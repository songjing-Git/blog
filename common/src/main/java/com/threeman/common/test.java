package com.threeman.common;

import java.util.HashMap;

public class test {

    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("a","b");
        objectObjectHashMap.put("a","c");
        Object a = objectObjectHashMap.get("a");
        System.out.println(a.hashCode());
        

        System.out.println(objectObjectHashMap);

    }

}
