package com.khalti.utils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;

public class ErrorUtil {

    /*Form error messages*/
    public static String EMPTY_ERROR = "This field is required";
    public static String MOBILE_ERROR = "Enter a valid mobile number";
    public static String PIN_ERROR = "Enter valid 4 digit PIN";
    public static String CODE_ERROR = "Enter valid 6 digit confirmation code";
    public static String GENERIC_ERROR = "An error occurred, please try again later";
    private static String UNAVAILABLE_ERROR = "Service temporarily unavailable";
    public static String GENERIC_JSON_ERROR = "{\"detail\":\"An error occurred, please try again later\"}";

    public static String parseError(String json, String statusCode) {
        if (EmptyUtil.isEmpty(json)) {
            json = GENERIC_JSON_ERROR;
        }
        HashMap<String, String> map = new HashMap<>();
        if (EmptyUtil.isNotNull(statusCode) && EmptyUtil.isNotEmpty(statusCode) && statusCode.equals("503")) {
            map.put("detail", UNAVAILABLE_ERROR);
        } else {
            try {
                JSONObject root = new JSONObject(json);
                if (root.has("detail")) {
                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    if (root.has("meta") && !(root.get("meta") + "").equals("{}")) {
                        map.putAll(new Gson().fromJson(JsonUtil.convertToJsonString(root.get("meta")), type));
                        root.remove("meta");
                    }
                    root.remove("error_data");//Remove this if error_data is needed
                    root.remove("meta");
                    map.putAll(new Gson().fromJson(root + "", type));
                } else {
                    if (root.has("non_field_error")) {
                        map.put("detail", JsonUtil.parseJsonArray(root.getString("non_field_error")));
                        root.remove("non_field_error");
                    }
                    if (root.has("error_key")) {
                        map.put("error_key", root.getString("error_key"));
                    }
                    Iterator keys = root.keys();
                    while (keys.hasNext()) {
                        String currentKey = (String) keys.next();
                        if (!currentKey.toLowerCase().contains("status") && !currentKey.toLowerCase().contains("error_key")) {
                            map.put(currentKey, JsonUtil.parseJsonArray(currentKey, json));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (map.isEmpty()) {
                map.put("detail", GENERIC_ERROR);
            }
        }
        map.put("status", statusCode);

        return JsonUtil.convertToJsonString(map);
    }

    public static String parseThrowableError(@Nullable String t, String statusCode) {
        HashMap<String, String> map = new HashMap<>();
        String error = null;
        if (EmptyUtil.isNull(t)) {
            t = "";
        }
        if (t.toLowerCase().contains("timed out")) {
            error = "Looks like you have an unstable network at the moment, please try again when network stabilizes";
        } else if (t.toLowerCase().contains("cannot connect") || t.toLowerCase().contains("failed to connect")) {
            error = "Looks like the server is taking too long to respond, please try again later";
        }

        if (EmptyUtil.isNull(error)) {
            error = GENERIC_ERROR;
        }
        map.put("status", statusCode);
        map.put("detail", error);
        return JsonUtil.convertToJsonString(map);
    }
}