package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityDistributionView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityDistributionItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/5.
 */
public interface ActivityDistributionService extends BaseService {

    PageResult<ActivityDistributionView> getPageList(PageParam<ActivityDistributionItem> pageParam);

    List<ActivityDistributionInfo> findAll();

    ActivityDistributionEntity save(ActivityDistributionEntity activityDistributionEntity);

    boolean delete(ActivityDistributionInfo activityDistributionInfo);

    boolean deleteById(String objId);

    ActivityDistributionEntity findOneById(String objId);


    List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(String keyword, String dealerCode);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllObjIdsByVehicleId(List<String> vehicleObjIds);


    List<ActivityVehicleView> findActivityVehicleItemsDistributionId(String distributionId);

    Boolean deleteActivityDistributionVehicleItem(String activityVehicleObjId);
}
