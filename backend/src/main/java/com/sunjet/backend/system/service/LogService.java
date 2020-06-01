package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.LogInfo;

/**
 * Created by lhj on 16/6/18.
 */
public interface LogService {

    //List<LogEntity> findAll();

    //void deleteById(String logId);

    LogInfo save(LogInfo logInfo);

    boolean delete(LogInfo logInfo);

    boolean delete(String objId);

    LogInfo findOne(String objId);

}
