package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.WebServiceAccessLogEntity;
import com.sunjet.backend.system.repository.WebServiceAccessLogRepository;
import com.sunjet.dto.system.admin.WebServiceAccessLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lhj on 16/6/17.
 * web service 接口访问日志
 */
@Transactional
@Service("webServiceAccessLogService")
public class WebServiceAccessLogServiceImpl implements WebServiceAccessLogService {

    /**
     * 获取数据层
     */
    @Autowired
    private WebServiceAccessLogRepository webServiceAccessLogRepository;


    /**
     * get entity by id
     *
     * @param objId 主键
     * @return
     */
    @Override
    public WebServiceAccessLogInfo findOne(String objId) {
        try {
            WebServiceAccessLogEntity webServiceAccessLogEntity = webServiceAccessLogRepository.findOne(objId);
            return entityToInfo(webServiceAccessLogEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增/修改  -- 对象
     *
     * @param webServiceAccessLogInfo vo 对象
     * @return
     */
    @Override
    public WebServiceAccessLogInfo save(WebServiceAccessLogInfo webServiceAccessLogInfo) {
        try {
            WebServiceAccessLogEntity entity = infoToEntity(webServiceAccessLogInfo);
            entity = webServiceAccessLogRepository.save(entity);
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 -- by id
     *
     * @param objId 主键
     */
    @Override
    public boolean delete(String objId) {
        try {
            webServiceAccessLogRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 -- 对象
     *
     * @param webServiceAccessLogInfo vo 对象
     */
    @Override
    public boolean delete(WebServiceAccessLogInfo webServiceAccessLogInfo) {
        try {
            WebServiceAccessLogEntity entity = infoToEntity(webServiceAccessLogInfo);
            webServiceAccessLogRepository.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * vo 对象转换为 entity对象
     *
     * @param info vo 对象
     * @return
     */
    private WebServiceAccessLogEntity infoToEntity(WebServiceAccessLogInfo info) {

        return WebServiceAccessLogEntity
                .WebServiceAccessLogEntityBuilder
                .aWebServiceAccessLogEntity()
                .withCreaterId(info.getCreaterId())
                .withCreaterName(info.getCreaterName())
                .withModifierId(info.getModifierId())
                .withType(info.getType())
                .withModifierName(info.getModifierName())
                .withOperateType(info.getOperateType())
                .withObjId(info.getObjId())
                .withBeginTime(info.getBeginTime())
                .withEnabled(info.getEnabled())
                .withEndTime(info.getEndTime())
                .withCreatedTime(info.getCreatedTime())
                .withParam(info.getParam())
                .withFailResult(info.getFailResult())
                .withModifiedTime(info.getModifiedTime())
                .withStatus(info.getStatus())
                .build();
    }

    /**
     * entity对象 对象转换为 vo对象
     *
     * @param entity entity 对象
     * @return
     */
    private WebServiceAccessLogInfo entityToInfo(WebServiceAccessLogEntity entity) {
        return WebServiceAccessLogInfo.
                WebServiceAccessLogInfoBuilder.
                aWebServiceAccessLogInfo()
                .withCreaterId(entity.getCreaterId())
                .withCreaterName(entity.getCreaterName())
                .withModifierId(entity.getModifierId())
                .withType(entity.getType())
                .withModifierName(entity.getModifierName())
                .withOperateType(entity.getOperateType())
                .withObjId(entity.getObjId())
                .withBeginTime(entity.getBeginTime())
                .withEnabled(entity.getEnabled())
                .withEndTime(entity.getEndTime())
                .withCreatedTime(entity.getCreatedTime())
                .withParam(entity.getParam())
                .withFailResult(entity.getFailResult())
                .withModifiedTime(entity.getModifiedTime())
                .withStatus(entity.getStatus())
                .build();
    }
}