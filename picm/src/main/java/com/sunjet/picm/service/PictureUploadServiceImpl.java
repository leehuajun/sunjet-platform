package com.sunjet.picm.service;

import com.sunjet.hessian.PictureUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author: lhj
 * @create: 2017-09-13 14:01
 * @description: 说明
 */
@Slf4j
@Service
public class PictureUploadServiceImpl implements PictureUploadService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public Boolean upload(String fileName, InputStream data) {
        System.out.println("filename : " + fileName);

        Boolean result = true;

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            long s = System.currentTimeMillis();
            inputStream = new BufferedInputStream(data);

            outputStream = new BufferedOutputStream(new FileOutputStream(uploadDirectory + fileName));
            byte[] buffer = new byte[8192];
            int b = inputStream.read(buffer, 0, buffer.length);
            while (b > 0) {
                outputStream.write(buffer, 0, b);
                b = inputStream.read(buffer, 0, buffer.length);
            }
            long e = System.currentTimeMillis();
            System.out.println((e-s));
            result = true;

        }catch (IOException ex) {
            ex.printStackTrace();
            result = false;
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return result;
        }
    }
}
