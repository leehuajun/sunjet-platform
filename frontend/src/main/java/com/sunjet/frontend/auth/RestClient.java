package com.sunjet.frontend.auth;

import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-13 15:38
 * @description: 说明
 */
@Component
public class RestClient {
//    private RestTemplate

    @Value("${custom.ip}")
    private String customIp;//校验ip

    @Value("${custom.pathUrl}")
    private String pathUrl;//服务器请求的url

    @Autowired
    private RestTemplate restTemplate;

    private <T> HttpEntity<T> getHttpEntity(HttpEntity<T> entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("CUSTOM_IP", customIp);
        headers.add("TOKEN", "abcdefg");
        if (entity != null) {
            return new HttpEntity<>(entity.getBody(), headers);
        } else {
            return new HttpEntity<>(null, headers);
        }
    }


    /**
     * 获取请求地址
     *
     * @param path /namespace/xxxx
     * @return
     */
    private String getPath(String path) {
        return pathUrl + path;
    }

    /**
     * 查询
     *
     * @param url
     * @param c
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> get(String url, Class<T> c) {
        setStartTime(url);
        ResponseEntity<T> responseEntity = restTemplate.exchange(getPath(url), HttpMethod.GET, getHttpEntity(null), c);
        printExecuteTime(url);
        return responseEntity;
    }

    /**
     * 查询
     *
     * @param url
     * @param httpEntity
     * @param c
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> get(String url, HttpEntity httpEntity, Class<T> c) {
        setStartTime(url);
        ResponseEntity<T> responseEntity = restTemplate.exchange(getPath(url), HttpMethod.POST, getHttpEntity(httpEntity), c);
        printExecuteTime(url);
        return responseEntity;
    }

    /**
     * 集合查询
     *
     * @param pathUrl
     * @param httpEntity
     * @param typeRef    new ParameterizedTypeReference<List<UserItem>>() {};
     * @return List<T>
     */
    public <T> List<T> findAll(String pathUrl, HttpEntity httpEntity, ParameterizedTypeReference<List<T>> typeRef) {
        try {
            setStartTime(pathUrl);
            //ParameterizedTypeReference<List<UserItem>> typeRef = new ParameterizedTypeReference<List<UserItem>>() {};
            ResponseEntity<List<T>> responseEntity = restTemplate.exchange(getPath(pathUrl), HttpMethod.POST, getHttpEntity(httpEntity), typeRef);
            List<T> list = responseEntity.getBody();
            printExecuteTime(pathUrl);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    ///**
    // * 分页
    // * @param pathUrl
    // * @param pageParam
    // * @param typeRef new ParameterizedTypeReference<List<UserItem>>() {};
    // * @return PageResult<T>
    // */
    //public <T> PageResult<T> getPageList(String pathUrl, PageParam<T> pageParam){
    //    try {
    //        setStartTime(pathUrl);
    //        ParameterizedTypeReference<PageResult<T>> typeRef = new ParameterizedTypeReference<PageResult<T>>() {};
    //        HttpEntity<PageParam> requestEntity = new HttpEntity<>(pageParam, null);
    //        ResponseEntity<PageResult<T>> responseEntity = restTemplate.exchange(getPath(pathUrl), HttpMethod.POST, getHttpEntity(requestEntity), typeRef);
    //        PageResult<T> pageResult = responseEntity.getBody();
    //        printExecuteTime(pathUrl);
    //        return pageResult;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return null;
    //    }
    //
    //}

    /**
     * 分页
     *
     * @param pathUrl
     * @param pageParam
     * @param <T>
     * @return
     */
    public <T> PageResult<T> getPageList(String pathUrl, PageParam<T> pageParam, ParameterizedTypeReference<PageResult<T>> typeRef) {
        try {
            setStartTime(pathUrl);
            //ParameterizedTypeReference<PageResult<UserItem>> typeRef = new ParameterizedTypeReference<PageResult<UserItem>>() {};
            HttpEntity<PageParam> requestEntity = new HttpEntity<>(pageParam, null);
            ResponseEntity<PageResult<T>> responseEntity = restTemplate.exchange(getPath(pathUrl), HttpMethod.POST, getHttpEntity(requestEntity), typeRef);
            PageResult<T> pageResult = responseEntity.getBody();
            printExecuteTime(pathUrl);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增
     *
     * @param url
     * @param httpEntity
     * @param c
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity httpEntity, Class<T> c) {
        setStartTime(url);
        ResponseEntity<T> responseEntity = restTemplate.exchange(getPath(url), HttpMethod.POST, getHttpEntity(httpEntity), c);
        printExecuteTime(url);
        return responseEntity;
    }

    /**
     * 保存集合
     *
     * @param pathUrl
     * @param httpEntity
     * @param typeRef
     * @param <T>
     * @return
     */
    public <T> List<T> post(String pathUrl, HttpEntity httpEntity, ParameterizedTypeReference<List<T>> typeRef) {
        try {
            setStartTime(pathUrl);
            //ParameterizedTypeReference<List<UserItem>> typeRef = new ParameterizedTypeReference<List<UserItem>>() {};
            ResponseEntity<List<T>> responseEntity = restTemplate.exchange(getPath(pathUrl), HttpMethod.POST, getHttpEntity(httpEntity), typeRef);
            List<T> list = responseEntity.getBody();
            printExecuteTime(pathUrl);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 修改
     *
     * @param url
     * @param httpEntity
     * @param c
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity httpEntity, Class<T> c) {
        setStartTime(url);
        ResponseEntity<T> responseEntity = restTemplate.exchange(getPath(url), HttpMethod.PUT, getHttpEntity(httpEntity), c);
        printExecuteTime(url);
        return responseEntity;
    }

    /**
     * 删除
     *
     * @param url
     * @param httpEntity
     * @param c
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity httpEntity, Class<T> c) {
        setStartTime(url);
        ResponseEntity<T> responseEntity = restTemplate.exchange(getPath(url), HttpMethod.DELETE, getHttpEntity(httpEntity), c);
        printExecuteTime(url);
        return responseEntity;
    }


    //方便测试
    private long startTime;

    private void setStartTime(String url) {
        System.out.println("\r\n\r\n");
        System.out.println("=================用户访问目标:" + url);
        startTime = System.currentTimeMillis();
    }

    private void printExecuteTime(String url) {
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        System.out.println("=================[" + url + "] 执行耗时 : " + executeTime + "ms");
    }
}
