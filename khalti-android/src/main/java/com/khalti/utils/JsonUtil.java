package com.khalti.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonUtil {

    private static String GENERIC_ERROR = "An error occurred, please try again later";

    public static HashMap<String, Object> getEBankingData(String jsonString) {
        if (EmptyUtil.isNotNull(jsonString) && EmptyUtil.isNotEmpty(jsonString)) {
            HashMap<String, Object> map = new HashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String currentKey = (String) keys.next();
                    Object o = jsonObject.get(currentKey);
                    map.put(currentKey, o.toString());
                }
                return map;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static String convertToJsonString(@NonNull Object o) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);
    }

    public static String parseJsonArray(@NonNull String json) {
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();

            while (keys.hasNext()) {
                String currentKey = (String) keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(currentKey);
                ArrayList<String> responseArray = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String response = jsonArray.getString(i);
                    responseArray.add(response);
                }
                String sep = "\n";
                for (int i = 0; i < responseArray.size(); i++) {
                    sb.append(responseArray.get(i)).append(sep);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return GENERIC_ERROR;
        }
        return sb.toString();
    }

    public static String parseJsonArray(@NonNull String key, @NonNull String json) {
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            ArrayList<String> responseArray = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String response = jsonArray.getString(i);
                responseArray.add(response);
            }
            String sep = "\n";
            for (int i = 0; i < responseArray.size(); i++) {
                sb.append(responseArray.get(i)).append(sep);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return GENERIC_ERROR;
        }
        return sb.toString();
    }

    public static HashMap<String, String> convertJsonStringToMap(@NonNull String s) {
        if (isJSONValid(s)) {
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            return new Gson().fromJson(s, type);
        } else {
            return new HashMap<String, String>() {{
                put("detail", "Something went wrong, please try again later");
            }};
        }
    }

    private static boolean isJSONValid(String s) {
        try {
            new JSONObject(s);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}