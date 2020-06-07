package com.github.code.controller;

import com.github.code.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompareSortController {

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

        List<UserEntity> sort = sort(list);
        System.out.println(Objects.toString(sort));

    }

    public static List<UserEntity> sort(List<UserEntity> list){
        //根据指定比较器产生的顺序对指定列表进行排序。
        Collections.sort(list, new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity o1, UserEntity o2) {
                //获取所需语言环境的 Collator，根据所需切换其他语言环境
                //Collator collator = Collator.getInstance(Locale.CANADA);
                Collator collator = Collator.getInstance(Locale.ENGLISH);
                return collator.compare(o1.getAddress(), o2.getAddress());
            }
        });
        return list;
    }



    public static List<UserEntity> sortByDate(List<UserEntity> list){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Collections.sort(list, new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity o1, UserEntity o2) {
                try {
                    return Long.valueOf(sdf.parse(o1.getBirthday()).getTime()).compareTo(sdf.parse(o2.getBirthday()).getTime());
                }catch (ParseException e){
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.error("ERROR:"+ e.getMessage());
                    return 0;
                }
            }
        });
        return list;
    }
}
