package com.khalti.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonUtil {
    public static HashMap<String, Object> getEBankingData(String jsonString) {
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
}