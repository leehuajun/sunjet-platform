package com.sunjet.frontend.utils.common;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sunjet.hessian.PictureUploadService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author: lhj
 * @create: 2017-09-13 14:06
 * @description: 说明f
 */
public class HessianUtil {

    public static Boolean uploadFile(String url, String filePath, String fileName) {
        try {
            HessianProxyFactory factory = new HessianProxyFactory();
            factory.setOverloadEnabled(true);
//          String url = "http://localhost:8080/HelloService";
            PictureUploadService pictureUploadService = (PictureUploadService) factory.create(PictureUploadService.class, url);
            InputStream data = new BufferedInputStream(new FileInputStream(filePath + "/" + fileName));
            pictureUploadService.upload(fileName, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
