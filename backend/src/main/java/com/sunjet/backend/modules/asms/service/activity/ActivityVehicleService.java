package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityVehicleService {

    ActivityVehicleEntity save(ActivityVehicleEntity activityVehicleEntity);

    boolean delete(ActivityVehicleInfo activityVehicleInfo);

    boolean deleteById(String objId);

    ActivityVehicleEntity findOne(String objId);

    PageResult<ActivityVehicleView> getPageList(PageParam<ActivityVehicleItem> pageParam);

    Integer findCountVehicleByActivityNoticeId(String activityNoticeId);

    List<ActivityVehicleInfo> saveActivityDistributionId(List<ActivityVehicleInfo> activityVehicleInfoList);

    PageResult<ActivityVehicleView> getactivityVehiclePageListScreenActivityDistributionIdIsNULL(PageParam<ActivityVehicleItem> pageParam);

    boolean deleteRels(String objId);

    List<ActivityVehicleInfo> findVehicleListByActivityNoticeId(String objId);

    List<ActivityVehicleView> searchActivityVehicleByActivityDistributionId(String objId, String keyword);

    ActivityVehicleView findOneById(String objId);

    boolean deleteActivityVehicleById(String activityVehicleId);

    boolean deleteByActivityNoticeId(String activityNoticeId);

    List<ActivityVehicleView> searchActivityVehicleByActivityDistributionId(String objId);

    List<ActivityVehicleEntity> saveList(List<ActivityVehicleEntity> list);

    List<String> findAllObjIdByVin(String vin);

    List<ActivityVehicleEntity> findAllByVehicleIds(List<String> vehicleObjIds);

    List<ActivityVehicleEntity> findAllByVehicleId(String vehicleId);
}
