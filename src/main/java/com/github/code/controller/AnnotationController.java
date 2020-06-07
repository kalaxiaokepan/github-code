/**
 * Copyright (C), 2020-01-07
 * FileName: AnnotationController
 * Author:   XXXX
 * Date:     2020/1/7 20:12
 * Description: 添加注释
 */
package com.github.code.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * <功能简要> <br>
 * <添加注释>
 *
 * @Author XXXX
 * @createTime 2020/1/7 20:12
 * @since 1.0.1
 */
public class AnnotationController {

    public static void main(String[] args) {
        Set<String> set1 = new HashSet<>();
        set1.add("1");
        set1.add("2");

        Set<String> set2 = new HashSet<>();
        set2.add("3");
        set2.add("4");

        set1.addAll(set2);

        for (String set: set1) {
            System.out.println(set);
        }
    }

}
