package com.utila;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileUtil {
    public static boolean doesFileExists(String url) {
        if (EmptyUtil.isNotNull(url)) {
            try {
                FileInputStream file = new FileInputStream(new File(url));
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
