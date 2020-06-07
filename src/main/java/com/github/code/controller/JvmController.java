package com.github.code.controller;

import java.util.ArrayList;

/**
 * JVM 测试类
 */
public class JvmController {
    byte[] a = new byte[1024*100];

    public static void main(String[] args) throws InterruptedException {
        ArrayList<JvmController> heapTest = new ArrayList<>();
        while(true) {
            heapTest.add(new JvmController());
            Thread.sleep(10);
        }
    }
}
