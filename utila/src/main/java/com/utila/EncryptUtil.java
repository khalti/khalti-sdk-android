package com.utila;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public static String hash(String s) {
        if (EmptyUtil.isNull(s)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            if (EmptyUtil.isNotNull(digest)) {
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest)
                    hexString.append(Integer.toHexString(0xFF & aMessageDigest));
                return hexString.toString();
            } else {
                return "";
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String hash256(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(s.getBytes()); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : digest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
