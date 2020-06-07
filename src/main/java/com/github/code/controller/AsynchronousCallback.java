package com.github.code.controller;

import com.github.code.entity.UserEntity;
import com.github.code.utils.ThreadPoolExecutorFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * 异步回调：
 *  Future
 *  Callable<T>
 */
public class AsynchronousCallback {

    public static void main(String[] args) {
        String[] addresss = {"北京市","上海市"};

        List<Future<List<UserEntity>>> futureList = new ArrayList<>();
        for (String address: addresss) {
            futureList.add(ThreadPoolExecutorFactory.executor.submit(new TaskCallable(address)));
        }

        List<UserEntity> users = new ArrayList<>();
        for (Future<List<UserEntity>> user: futureList) {
            try {
                List<UserEntity> list = user.get();
                if (!CollectionUtils.isEmpty(list)){
                    users.addAll(list);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(users);
    }
}

class TaskCallable implements Callable<List<UserEntity>> {

    private String address;

    public TaskCallable(String address) {
        this.address = address;
    }

    @Override
    public List<UserEntity> call() throws InterruptedException {
        System.out.println(address);
        UserServiceImpl userService = new UserServiceImpl();
        List<UserEntity> userServiceById = userService.findById(address);
        return userServiceById;
    }
}

class UserServiceImpl {

    public List<UserEntity> findById(String address) throws InterruptedException {
        List<UserEntity> list = new ArrayList<>();
        UserEntity userEntity1 = new UserEntity(1, "小红", "女", 18,"北京市");
        UserEntity userEntity2 = new UserEntity(2, "小刚", "男", 18,"北京市");
        UserEntity userEntity3 = new UserEntity(3, "小青", "女", 18,"北京市");
        UserEntity userEntity4 = new UserEntity(4, "小率", "男", 18,"北京市");
        UserEntity userEntity5 = new UserEntity(4, "小率", "男", 18,"北京市");

        UserEntity userEntity6 = new UserEntity(6, "小1", "女", 18,"上海市");
        UserEntity userEntity7 = new UserEntity(7, "小2", "男", 18,"上海市");
        UserEntity userEntity8 = new UserEntity(8, "小3", "女", 18,"上海市");
        UserEntity userEntity9 = new UserEntity(9, "小4", "男", 18,"上海市");
        UserEntity userEntity10 = new UserEntity(10, "小5", "男", 18,"上海市");

        list.add(userEntity1);
        list.add(userEntity2);
        list.add(userEntity3);
        list.add(userEntity4);
        list.add(userEntity5);

        list.add(userEntity6);
        list.add(userEntity7);
        list.add(userEntity8);
        list.add(userEntity9);
        list.add(userEntity10);

        List<UserEntity> result = new ArrayList<>();

        for (UserEntity user: list ) {
            if (user.getAddress().equals(address)){
                result.add(user);
            }
        }
        return result;
    }
}