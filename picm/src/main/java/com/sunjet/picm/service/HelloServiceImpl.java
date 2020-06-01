package com.sunjet.picm.service;

import com.sunjet.hessian.HelloService;
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
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        log.info("从客户端传递过来的name:" + name);
        return "Hello "+ name;
    }
}
