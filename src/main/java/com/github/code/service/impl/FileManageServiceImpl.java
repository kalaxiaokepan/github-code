/**
 * Copyright (C), 2020-02-26
 * FileName: FileManageServiceImpl
 * Author:   heyanbo
 * Date:     2020/2/26 22:33
 * Description: 文件管理接口实现类
 */
package com.github.code.service.impl;

import com.github.code.service.FileManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * <功能简要> <br>
 * <文件管理接口实现类>
 *
 * @Author heyanbo
 * @createTime 2020/2/26 22:33
 * @since
 */
@Service
public class FileManageServiceImpl implements FileManageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManageService.class);

    @Value("${file_hostpath}")
    private String fileHostPath;

    private int SIZE=1024;

    @Override
    public String upload(MultipartFile file) {
        File test = new File(fileHostPath+file.getOriginalFilename());
        if (!test.exists()){
            test.mkdirs();
        }
        try {
            file.transferTo(test);
        }catch (IOException e){
            LOGGER.error(file.getOriginalFilename()+"文件上传失败", e);
        }
        return "文件上传成功";
    }

    public String download(String fileName, HttpServletResponse response){
        File file = new File(fileHostPath + fileName);
        if (!file.exists()){
            return "文件不存在";
        }
        byte[] bytes = new byte[SIZE];
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            outputStream = response.getOutputStream();
            int length;
            while ((length = bufferedInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        }catch (Exception e){
            LOGGER.error("文件下载失败", e);
        }finally {
            try {
                if (bufferedInputStream != null){
                    bufferedInputStream.close();
                }

                if (outputStream != null){
                    outputStream.close();
                }

                if (fileInputStream != null){
                    fileInputStream.close();
                }
            }catch (IOException e){
                LOGGER.error(e.getMessage(), e);
            }
        }
        return "success";
    }
}
