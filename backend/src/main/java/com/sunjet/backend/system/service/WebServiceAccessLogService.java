package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.WebServiceAccessLogInfo;

/**
 * Created by lhj on 16/6/17.
 */
public interface WebServiceAccessLogService {

    //WebServiceAccessLogEntity findOne(String objId);
    //List<WebServiceAccessLogEntity> findAll();

    WebServiceAccessLogInfo findOne(String objId);

    WebServiceAccessLogInfo save(WebServiceAccessLogInfo webServiceAccessLogInfo);

    boolean delete(String objId);

    boolean delete(WebServiceAccessLogInfo webServiceAccessLogInfo);
}
