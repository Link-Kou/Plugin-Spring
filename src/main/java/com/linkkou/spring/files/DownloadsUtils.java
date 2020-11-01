package com.linkkou.spring.files;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

/**
 * @author lk
 * @version 1.0
 * @date 2020/11/1 11:06
 */
public class DownloadsUtils {

    /**
     *
     * @param file
     * @param suffix
     * @return
     */
    public ResponseEntity<byte[]> 构建springDownloads(File file, String suffix) {
        try {
            if (file.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                //指定文件名称以及后缀
                headers.setContentDispositionFormData("attachment", file.getName() + suffix);
                return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
    }
}
