/**
 * Copyright (C), 2020-02-26
 * FileName: FileManagerController
 * Author:   heyan
 * Date:     2020/2/26 22:24
 * Description: 文件管理
 */
package com.github.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.code.service.FileManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * <功能简要> <br>
 * <文件管理>
 *
 * @Author heyan
 * @createTime 2020/2/26 22:24
 * @since
 */
@RestController
public class FileManagerController {

    @Autowired
    private FileManageService fileManageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/upload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file){
        if (Objects.isNull(file)){
            return "文件上传失败，请重新选择文件";
        }
        return fileManageService.upload(file);
    }

    @GetMapping("/download")
    @ResponseBody
    public String fileDownload(@RequestParam("fileName") String fileName, HttpServletResponse response){
        if (Objects.isNull(fileName)){
            return "文件下载失败，请选择文件要下载的文件";
        }
        return fileManageService.download(fileName, response);
    }


    /**
     * @param fileName 待分割的文件名 例：nginx.tar
     * @return  key
     */
    @GetMapping("/cutFile")
    @ResponseBody
    public String cutFile(String fileName){
        String key = String.valueOf(System.currentTimeMillis())+"-"+ fileName+"-key";
        stringRedisTemplate.boundValueOps(key).set("start");
        stringRedisTemplate.expire(key, 10, TimeUnit.MINUTES);

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                List<String> fileNames = fileManageService.cutFile(fileName);
                if (CollectionUtils.isEmpty(fileNames)){
                    stringRedisTemplate.boundValueOps(key).set("failed");
                    stringRedisTemplate.expire(key, 1, TimeUnit.MINUTES);
                }

                if (!CollectionUtils.isEmpty(fileNames)){
                    stringRedisTemplate.boundValueOps(key).set(JSONObject.toJSONString(fileNames));
                    stringRedisTemplate.expire(key, 2, TimeUnit.MINUTES);
                }
            }
        });
        //返回key
        return key;
    }

    /**
     * @param cutFileName 任意一个分段文件名,例：1591604609899-1-redis.tar
     * @param chunks 分段总数
     * @return
     */
    @GetMapping("/merageFile")
    @ResponseBody
    public String merageFile(@RequestParam String cutFileName,
                             @RequestParam int chunks) throws IOException {
        return fileManageService.merageFile(cutFileName, chunks);
    }
}
