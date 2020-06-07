package com.github.code.utils;

import com.spotify.docker.client.DefaultDockerClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class DockerClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerClientUtil.class);

    private static String DEFAULT_PORT = "2375";

    private static long CONNECT_TIMEOUT_MILLIS = 10000L;

    public static DefaultDockerClient getDockerClient(String hostPath, String port) throws Exception{
        if (StringUtils.isBlank(port)){
            port = DEFAULT_PORT;
        }
        DefaultDockerClient dockerClient;
        try {
            dockerClient = DefaultDockerClient.builder()
                    .connectTimeoutMillis(CONNECT_TIMEOUT_MILLIS)
                    .uri(URI.create("http://" + hostPath + ":" + port)).build();
        }catch (Exception e){
            LOGGER.error("DockerClient连接超时", e.getMessage());
            throw new Exception(e.getMessage());
        }
        return dockerClient;
    }

}
