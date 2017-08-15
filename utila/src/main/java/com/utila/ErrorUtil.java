package com.utila;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ErrorUtil {
    public static String parseError(String json) {
        String defaultError = "Something went wrong.";
        if (EmptyUtil.isNull(json)) {
            return defaultError;
        }
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();

            while (keys.hasNext()) {
                String currentKey = (String) keys.next();
                Object o = jsonObject.get(currentKey);

                if (o instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) o;
                    ArrayList<String> responseArray = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String response = jsonArray.getString(i);
                        responseArray.add(response);
                    }
                    String sep = "\n";
                    for (int i = 0; i < responseArray.size(); i++) {
                        sb.append(responseArray.get(i)).append(sep);
                    }
                } else {
                    sb.append(o);
                }
            }
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultError;
        }
    }
}
