package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityMaintenanceView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务活动维修
 * Created by lhj on 16/9/17.
 */
public interface ActivityMaintenanceService extends BaseService {

    ActivityMaintenanceInfo save(ActivityMaintenanceInfo activityMaintenanceInfo);

    boolean delete(ActivityMaintenanceInfo activityMaintenanceInfo);

    boolean delete(String objId);

    ActivityMaintenanceInfo findAll(String objId);

    PageResult<ActivityMaintenanceView> getPageList(PageParam<ActivityMaintenanceItem> pageParam);

    ActivityMaintenanceEntity findOneById(String objId);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllIdByVehcicleIds(List<String> vehicleObjIds);

    List<ActivityMaintenanceInfo> findAllByVehicleIds(List<String> vehicleId);

    Boolean desertTask(String objId);


    //List<ActivityMaintenanceEntity> findall();
    //
    //void save(ActivityMaintenanceEntity activityMaintenanceRequest);
    //
    //ActivityMaintenanceEntity findOneWithVehicles(String objId);
    //
    //ActivityMaintenanceEntity findOneWithOthers(String objId);
    //
    //void deleteEntity(ActivityMaintenanceEntity entity);
    //
    //ActivityMaintenanceEntity findOneWithVehicleById(String objId);
}
