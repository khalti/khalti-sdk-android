package khalti.utils;


import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
    public static boolean isSuccessFul(Integer statusCode) {
        return statusCode >= 200 && statusCode <= 299;
    }

    public static String getPostData(HashMap<String, Object> map) {
        String postData = "";
        if (EmptyUtil.isNotNull(map) && EmptyUtil.isNotEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                postData = "&" + entry.getKey() + entry.getValue();
            }
        }

        return postData;
    }
}
