/**
 * Copyright (C), 2020-02-26
 * FileName: FileManageServiceImpl
 * Author:   heyanbo
 * Date:     2020/2/26 22:33
 * Description: 文件管理接口实现类
 */
package com.github.code.service.impl;

import com.github.code.service.FileManageService;
import com.github.code.utils.CutFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

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

    @Value("${save_addr}")
    private String saveAddr;

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

    @Override
    public List<String> cutFile(String fileName) {
        //待分片文件在主机上的路径
        String filePath = saveAddr + fileName;

        File file = new File(filePath);
        //分片文件的大小（字节）
        Long byteSize = 52428800L;
        List<String> fileNames = new CutFileUtil().cutFileBySize(filePath, byteSize, saveAddr);
        return fileNames;
    }

    @Override
    public String merageFile(String cutFileName, int chunks) throws IOException {
        int indexOf = cutFileName.indexOf("-");
        String timeStream = cutFileName.substring(0, indexOf);
        //段数+文件名+后缀名
        String substring = cutFileName.substring(indexOf + 1, cutFileName.length());
        int indexOf1 = substring.indexOf("-");
        //文件名+后缀名
        String fileName = substring.substring(indexOf1+1, substring.length());
        File file = new File(saveAddr+fileName);
        if (file.exists()){
            file.delete();
            LOGGER.info("覆盖已经存在的文件");
        }
        BufferedOutputStream destOutputStream = new BufferedOutputStream(new FileOutputStream(saveAddr+fileName));
        for (int i = 1; i <= chunks ; i++) {
            //循环将每个分片的数据写入目标文件
            byte[] fileBuffer = new byte[1024];//文件读写缓存
            int readBytesLength = 0; //每次读取字节数
            File sourceFile = new File(saveAddr+timeStream+"-"+i+"-"+fileName);
            BufferedInputStream sourceInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            LOGGER.info("开始合并分段文件："+timeStream+"-"+i+"-"+fileName);
            while ((readBytesLength = sourceInputStream.read(fileBuffer))!=-1){
                destOutputStream.write(fileBuffer, 0 , readBytesLength);
            }
            sourceInputStream.close();
            LOGGER.info("合并分段文件完成："+timeStream+"-"+i+"-"+fileName);
            //分片合并后删除
            boolean delete = sourceFile.delete();
            if (delete){
                LOGGER.info(timeStream+"-"+i+"-"+fileName+"删除完成");
            }
        }
        destOutputStream.flush();
        destOutputStream.close();
        return fileName+"合并完成";
    }
}
