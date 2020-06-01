package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.OperationEntity;
import com.sunjet.backend.system.entity.view.OperationView;
import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.OperationItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * 操作列表
 * Created by lhj on 16/6/18.
 */
public interface OperationService {
    OperationInfo save(OperationInfo operationInfo);

    boolean delete(OperationInfo operationInfo);

    boolean delete(String objId);

    List<OperationInfo> findAll();

    PageResult<OperationView> getPageList(PageParam<OperationItem> pageParam);

    OperationInfo findOneById(String objId);


    List<OperationEntity> infoListToEntityList(List<OperationInfo> infoList);

    List<OperationInfo> entityListToInfoList(List<OperationEntity> entityList);
}
