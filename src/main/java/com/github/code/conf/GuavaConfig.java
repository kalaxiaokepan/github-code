package com.github.code.conf;

import com.github.code.utils.DockerClientUtil;
import com.google.common.cache.*;
import com.spotify.docker.client.DefaultDockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuavaConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaConfig.class);

    public LoadingCache getDockerClient(){
        LoadingCache<Object, Object> loadingCache = CacheBuilder.newBuilder()
                //设置并发级别，指可以同时进行缓存的线程数
                .concurrencyLevel(5)
                //设置缓存最大容量，超过之后就会按照LUR算反来移除缓存项
                .maximumSize(5)
                //设置缓存容器的初始化容量
                .initialCapacity(2)
                //设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        LOGGER.info("缓存移除通知：" + removalNotification.getKey() + "被移除, 移除原因:" + removalNotification.getCause());
                    }
                })
                //在缓存不存在时通过cacheLoader实现自动加载缓存
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public DefaultDockerClient load(Object key) throws Exception {
                        String[] split = ((String) key).split(":");
                        DefaultDockerClient dockerClient = DockerClientUtil.getDockerClient(split[0], split[1]);
                        return dockerClient;
                    }
                });

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("-----------------guava本地缓存初始化成功------------------");
        }
        return loadingCache;
    }
}
