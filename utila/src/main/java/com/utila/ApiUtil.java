package com.utila;


public class ApiUtil {
    public static boolean isSuccessFul(Integer statusCode) {
        return statusCode >= 200 && statusCode <= 299;
    }
}
