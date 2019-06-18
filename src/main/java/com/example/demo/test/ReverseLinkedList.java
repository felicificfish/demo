package com.example.demo.test;

import java.util.Collections;
import java.util.LinkedList;

/**
 * LinkedList倒排
 *
 * @author zhou.xy
 * @since 2019/6/18
 */
public class ReverseLinkedList {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        int i = 0;
        while (i < 6) {
            list.add(i);
            i++;
        }
        Collections.reverse(list);
        System.out.println(list);
    }
}
