package com.linkkou.spring.files;

import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 文件辅助
 *
 * @author lk
 * @version 1.0
 * @date 2020/4/28 10:35
 */
public class FilesUtils {

    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getMD5(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
