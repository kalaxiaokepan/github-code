/**
 * Copyright (C), 2020-02-26
 * FileName: FileManageService
 * Author:   heyan
 * Date:     2020/2/26 22:29
 * Description: 文件管理接口
 */
package com.github.code.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * <功能简要> <br>
 * <文件管理接口>
 *
 * @Author heyanbo
 * @createTime 2020/2/26 22:29
 * @since
 */
public interface FileManageService {

    public String upload(MultipartFile file);

    String download(String fileName, HttpServletResponse response);

    /**
     * 文件分割
     * @param fileName
     * @return
     */
    List<String> cutFile(String fileName);

    String merageFile(String cutFileName, int chunks) throws FileNotFoundException, IOException;
}
