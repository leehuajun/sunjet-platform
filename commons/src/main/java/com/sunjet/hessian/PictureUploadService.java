package com.sunjet.hessian;

import java.io.InputStream;

/**
 * @author: lhj
 * @create: 2017-09-13 13:58
 * @description: 说明
 */
public interface PictureUploadService {
    Boolean upload(String fileName, InputStream data);
}
