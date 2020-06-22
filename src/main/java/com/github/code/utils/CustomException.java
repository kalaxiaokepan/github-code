/**
 * Copyright (C), 2020-06-18
 * FileName: CustomException
 * Author:   heyanbo
 * Date:     2020/6/18 0:03
 * Description: 自定义异常基础类
 */
package com.github.code.utils;

/**
 * <功能简要> <br>
 * <自定义异常基础类>
 *
 * @Author heyanbo
 * @createTime 2020/6/18 0:03
 * @since
 */
public class CustomException extends Exception {

    /**
     * 异常错误码
     */
    private String errorCode;

    public CustomException(String errorCode){
        this.errorCode = errorCode;
    }

    public CustomException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
