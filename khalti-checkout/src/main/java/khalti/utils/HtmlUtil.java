package khalti.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public static String getHrefLink(String htmlString) {
        Pattern pattern = Pattern.compile("<a href='(.*?)'>(.*?)");
        Matcher matcher = pattern.matcher(htmlString);

        if (matcher.find()) {
            return matcher.group(1).split("'")[0];
        }

        return "";
    }
}
