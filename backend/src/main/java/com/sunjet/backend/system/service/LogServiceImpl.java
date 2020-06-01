package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.LogEntity;
import com.sunjet.backend.system.repository.LogRepository;
import com.sunjet.dto.system.admin.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lhj on 16/6/17.
 * 系统日志
 */
@Transactional
@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * 保存
     *
     * @param logInfo
     * @return
     */
    @Override
    public LogInfo save(LogInfo logInfo) {
        try {
            LogEntity entity = logRepository.save(infoToEntity(logInfo));
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 -->  通过 logInfo 对象
     *
     * @param logInfo
     * @return
     */
    @Override
    public boolean delete(LogInfo logInfo) {
        try {
            LogEntity entity = infoToEntity(logInfo);
            logRepository.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 -->  通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            logRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 objId 查找实体
     *
     * @param objId
     * @return
     */
    @Override
    public LogInfo findOne(String objId) {
        try {
            LogEntity entity = logRepository.findOne(objId);
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * info 转为 entity
     *
     * @param logInfo
     * @return
     */
    private LogEntity infoToEntity(LogInfo logInfo) {
        return LogEntity.LogEntityBuilder
                .aLogEntity()
                .withCreaterId(logInfo.getCreaterId())
                .withCreaterName(logInfo.getCreaterName())
                .withModifierId(logInfo.getModifierId())
                .withModifierName(logInfo.getModifierName())
                .withObjId(logInfo.getObjId())
                .withEnabled(logInfo.getEnabled())
                .withMessage(logInfo.getMessage())
                .build();
    }


    /**
     * entity  转为 info
     *
     * @param logEntity
     * @return
     */
    private LogInfo entityToInfo(LogEntity logEntity) {
        return LogInfo.LogInfoBuilder
                .aLogInfo()
                .withObjId(logEntity.getObjId())
                .withCreaterId(logEntity.getCreaterId())
                .withEnabled(logEntity.getEnabled())
                .withCreaterName(logEntity.getCreaterName())
                .withModifierId(logEntity.getModifierId())
                .withModifierName(logEntity.getModifierName())
                .withMessage(logEntity.getMessage())
                .build();
    }
}