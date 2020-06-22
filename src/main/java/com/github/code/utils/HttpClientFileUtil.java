/**
 * Copyright (C), 2020-06-11
 * FileName: HttpClientFileUtil
 * Author:   heyanbo
 * Date:     2020/6/11 12:33
 * Description: HttpClient文件上传和下载
 */
package com.github.code.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * <功能简要> <br>
 * <HttpClient文件上传和下载>
 *
 * @Author heyanbo
 * @createTime 2020/6/11 12:33
 * @since
 */
public class HttpClientFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientFileUtil.class);

    public static final int CONNECTION_TIMEOUT = 35000;
    public static final int CONNECTION_REQUEST_TIMEOUT = 35000;
    public static final int SOCKET_TIMEOUT = 60000;
    public static final String UTF_8 = "UTF-8";


    public static String upload(String token, String url, MultipartFile file, String fileParamName, Map<String, String> otherParams){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        String fileName = file.getOriginalFilename();
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("DataEncoding", UTF_8);
            httpPost.setHeader("token", token);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("utf-8"));
            //解决返回中文乱码问题
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
            //模拟浏览器表单提交
            for (Map.Entry<String, String> entry : otherParams.entrySet()){
                builder.addTextBody(entry.getKey(), entry.getValue());
            }

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (Objects.nonNull(responseEntity)){
                //将返回值转化为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName(UTF_8));
            }
        } catch (Exception e) {
            LOGGER.error("上传文件异常，异常信息:"+e);
        }finally {
            try {
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void downLoad(String url, Map<String, String> map, String remoteFileName, String localFileName){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        map.put("fileName", remoteFileName);
        String downUrl = getUrl(url, map);
        String fileUrl = downUrl.replace("downLoad=false", "downLoad=true");
        LOGGER.info("downLoad url is :"+fileUrl);
        HttpGet httpGet = new HttpGet(fileUrl);
        httpGet.setHeader("fileName", remoteFileName);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        FileOutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            File storeFile = new File(localFileName);
            inputStream = httpResponse.getEntity().getContent();

            byte[] buff = new byte[30*1024*1024];
            int len = 0;
            while ((len = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            LOGGER.error("get下载文件请求错误："+e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    LOGGER.error("输入流关闭失败:"+e);
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                }catch (IOException e){
                    LOGGER.error("输出流关闭失败:"+e);
                }
            }

            if (httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOGGER.error("httpResponse关闭失败："+e);
                }
            }
            if (httpClient != null){
                try {
                    httpClient.close();
                }catch (IOException e){
                    LOGGER.error("httpClient连接关闭失败："+e);
                }
            }
        }

    }

    public static String getUrl(String url, Map<String, String> map){
        int i=0;
        for (Map.Entry<String, String> entry:map.entrySet()){
            if (i==0){
                url+= "?"+entry.getKey()+"="+entry.getValue();
            }else {
                url+= "&"+entry.getKey()+"="+entry.getValue();
            }
            i++;
        }
        return url;
    }
}
