package com.khalti.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
    public static boolean isSuccessFul(Integer statusCode) {
        return statusCode >= 200 && statusCode <= 299;
    }

    public static String getPostData(HashMap<String, String> map) {
        String postData = "";
        try {
            if (EmptyUtil.isNotNull(map) && EmptyUtil.isNotEmpty(map)) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    postData = "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue() + "", "UTF-8");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData;
    }
}
