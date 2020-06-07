/**
 * Copyright (C), 2020-02-26
 * FileName: FileManagerController
 * Author:   heyan
 * Date:     2020/2/26 22:24
 * Description: 文件管理
 */
package com.github.code.controller;

import com.github.code.service.FileManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
}
