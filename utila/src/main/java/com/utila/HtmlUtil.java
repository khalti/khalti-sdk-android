package com.utila;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public static HashMap<String, Object> getData(String htmlString) {
        HashMap<String, Object> map = new HashMap<>();
        Pattern pattern = Pattern.compile("<input.*name=\"(.*?)\" value=\"(.*?)\">");
        Matcher matcher = pattern.matcher(htmlString);

        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }
}
