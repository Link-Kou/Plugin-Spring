package com.plugin.javawidget.encryption;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密兼容 JS 、C# 、PHP
 * 加密结果默认为小写
 *
 * @author lk
 * @date 2018/9/23 14:07
 */
public class MD5Utils {

    @Deprecated
    public static String getEncryption(String originString) throws UnsupportedEncodingException {
        String result = "";
        if (originString != null) {
            try {
                // 指定加密的方式为MD5
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 进行加密运算
                byte bytes[] = md.digest(originString.getBytes("UTF-8"));
                for (int i = 0; i < bytes.length; i++) {
                    // 将整数转换成十六进制形式的字符串 这里与0xff进行与运算的原因是保证转换结果为32位
                    String str = Integer.toHexString(bytes[i] & 0xFF);
                    if (str.length() == 1) {
                        str += "F";
                    }
                    result += str;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String encrypt(String data) {
        return encrypt(data, false);
    }

    public static String encrypt(byte[] data) {
        return encrypt(data, false);
    }

    public static String encrypt(InputStream data) throws IOException {
        return encrypt(data, false);
    }

    public static String encrypt(String data, boolean upper) {
        return toUpperCase(DigestUtils.md5Hex(data), upper);
    }

    public static String encrypt(byte[] data, boolean upper) {
        return toUpperCase(DigestUtils.md5Hex(data), upper);
    }

    public static String encrypt(InputStream data, boolean upper) throws IOException {
        return toUpperCase(DigestUtils.md5Hex(data), upper);
    }

    /**
     * 大写字符串
     * @param data
     * @param upper
     * @return
     */
    private static String toUpperCase(String data, boolean upper) {
        if (upper) {
            return data.toUpperCase();
        }
        return data;
    }

}
