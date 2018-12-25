package com.example.demo.test;

import java.util.ArrayList;

/**
 * 堆栈溢出测试
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class OverflowTest {
    int num = 1;

    public static void main(String[] args) {
        OverflowTest t = new OverflowTest();
        t.testHeap();
//        t.testStack();
    }

    public void testHeap() {
        for (; ; ) {
            ArrayList list = new ArrayList(2000);
        }
    }

    public void

    testStack() {
        num++;
        this.testStack();
    }
}
