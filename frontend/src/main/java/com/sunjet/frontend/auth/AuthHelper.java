package com.sunjet.frontend.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author: lhj
 * @create: 2017-07-03 22:20
 * @description: 说明
 */
public class AuthHelper {

    public final static HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("CUSTOM_IP", "127.0.0.1");
        headers.add("TOKEN", "abcdefg");

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        return requestEntity;
    }
}
