package com.github.code.utils;


/**
 * 
 * http状态码判断
 *
 *
 */
public class HttpStatusUtil {
	
	public static boolean isSuccessStatus(int statusCode){
        return String.valueOf(statusCode).startsWith("2");
    }

}
