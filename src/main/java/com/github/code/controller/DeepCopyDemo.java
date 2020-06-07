/**
 * Copyright (C), 2020-03-05
 * FileName: DeepCopyDemo
 * Author:   heyanbo
 * Date:     2020/3/5 15:43
 * Description: 使用序列化流实现对List的深Copy
 */
package com.github.code.controller;

import com.github.code.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <功能简要> <br>
 * <使用序列化流实现对List的深Copy>
 *
 * @Author heyanbo
 * @createTime 2020/3/5 15:43
 * @since
 */
public class DeepCopyDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeepCopyDemo.class);

    public static Integer INDEX  = 0;

    public static void main(String[] args) {
        UserEntity userEntity1 = new UserEntity(0, "张无忌","女",1 , "beijignshi");
        UserEntity userEntity2 = new UserEntity(1, "李寻欢","女",4 , "tianjinshi");
        UserEntity userEntity3 = new UserEntity(3, "项羽","男",0 , "chuguo");
        UserEntity userEntity4 = new UserEntity(4, "刘邦","男",8 , "hanchao");

        List<UserEntity> list = new ArrayList<>();
        list.add(userEntity2);
        list.add(userEntity1);
        list.add(userEntity4);
        list.add(userEntity3);

        System.out.println("----deep copy front----");
        for (UserEntity userEntity: list) {
            System.out.println(userEntity.toString());
        }

        List<UserEntity> copyResult = deepCopy(list);
        System.out.println("----deep copy after----");
        copyResult.get(INDEX).setUsername("夷陵老祖");
        for (UserEntity userEntity: copyResult) {
            System.out.println(userEntity.toString());
        }

    }

    public static <T> List<T> deepCopy(List<T> list) {
        //判断是否实现序列化
        if (list instanceof Serializable){
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try {
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(list);

                ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                ObjectInputStream inStream = new ObjectInputStream(byteIn);
                List<T> ts = (List<T>) inStream.readObject();
                return ts;
            } catch (Exception e) {
                LOGGER.error("序列化Copy失败", e);
                e.printStackTrace();
            }
        }
        return null;
    }
}
