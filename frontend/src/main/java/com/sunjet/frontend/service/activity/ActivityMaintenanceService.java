package com.sunjet.frontend.service.activity;

import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.asm.CommissionPartService;
import com.sunjet.frontend.service.asm.GoOutService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/1.
 * 活动服务
 */
@Slf4j
@Service("activityMaintenanceService")
public class ActivityMaintenanceService {


    @Autowired
    private RestClient restClient;

    @Autowired
    private ActivityDistributionService activityDistributionService;
    @Autowired
    private ActivityVehicleService activityVehicleService;
    @Autowired
    private CommissionPartService commissionPartService;
    @Autowired
    private GoOutService goOutService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */

    public PageResult<ActivityMaintenanceItem> getPageList(PageParam<ActivityMaintenanceItem> pageParam) {
        try {
            return restClient.getPageList("/activityMaintenance/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ActivityMaintenanceItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityMaintenanceServicelmpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objid删除实体
     *
     * @param objId
     * @return
     */

    public boolean deleteByObjId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/activityMaintenance/delete", requestEntity, Boolean.class);

            log.info("ActivityMaintenanceServicelmpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityMaintenanceServicelmpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过obJid查找一个实体
     *
     * @param objId
     * @return
     */

    public ActivityMaintenanceInfo findOneById(String objId) {
        ResponseEntity<ActivityMaintenanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/activityMaintenance/findOneById", requestEntity, ActivityMaintenanceInfo.class);
            ActivityMaintenanceInfo activityMaintenanceInfo = responseEntity.getBody();
            if (StringUtils.isNotBlank(activityMaintenanceInfo.getActivityDistributionId())) {
                //获取活动分配单
                ActivityDistributionInfo activityDistributionInfo = activityDistributionService.findOneById(activityMaintenanceInfo.getActivityDistributionId());

                activityMaintenanceInfo.setActivityDistributionId(activityDistributionInfo.getObjId());
                activityMaintenanceInfo.setActivityDistribution(activityDistributionInfo);
            }
            //通过活动服务单Id 获取活动车辆
            if (StringUtils.isNotBlank(activityMaintenanceInfo.getActivityVehicleId())) {
                activityMaintenanceInfo.setActivityVehicleItem(activityVehicleService.findOneById(activityMaintenanceInfo.getActivityVehicleId()));
            }
            //通过活动服务单Id 获取维修配件
            activityMaintenanceInfo.setCommissionParts(commissionPartService.findAllByActivityMaintenanceObjId(activityMaintenanceInfo.getObjId()));
            //通过服务单Id 获取外出活动
            activityMaintenanceInfo.setGoOuts(goOutService.findAllByActivityMaintenanceObjId(activityMaintenanceInfo.getObjId()));
            return activityMaintenanceInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存实体
     *
     * @param activityMaintenanceRequest
     * @return
     */

    public ActivityMaintenanceInfo save(ActivityMaintenanceInfo activityMaintenanceRequest) {
        ResponseEntity<ActivityMaintenanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityMaintenanceRequest, null);
            responseEntity = restClient.get("/activityMaintenance/save", requestEntity, ActivityMaintenanceInfo.class);
            activityMaintenanceRequest = responseEntity.getBody();
            if (StringUtils.isNotBlank(activityMaintenanceRequest.getActivityDistributionId())) {
                //获取活动分配单
                ActivityDistributionInfo activityDistributionInfo = activityDistributionService.findOneById(activityMaintenanceRequest.getActivityDistributionId());
                activityMaintenanceRequest.setActivityDistributionId(activityDistributionInfo.getObjId());
                activityMaintenanceRequest.setActivityDistribution(activityDistributionInfo);
            }
            //通过活动服务单Id 获取活动车辆
            if (StringUtils.isNotBlank(activityMaintenanceRequest.getActivityVehicleId())) {
                activityMaintenanceRequest.setActivityVehicleItem(activityVehicleService.findOneById(activityMaintenanceRequest.getActivityVehicleId()));
            }

            return activityMaintenanceRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 启动流程
     *
     * @param activityMaintenanceRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(ActivityMaintenanceInfo activityMaintenanceRequest, ActiveUser activeUser) {
        ResponseEntity<Map> responseEntity = null;
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = UserInfo.UserInfoBuilder.anUserInfo()
                .withLogId(activeUser.getLogId())
                .withAgency(activeUser.getAgency() == null ? null : activeUser.getAgency())
                .withDealer(activeUser.getDealer() == null ? null : activeUser.getDealer())
                .withName(activeUser.getUsername())
                .withObjId(activeUser.getUserId())
                .withRoles(activeUser.getRoles())
                .withUserType(activeUser.getUserType())
                .build();
        map.put("entity", activityMaintenanceRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/activityMaintenance/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ActivityMaintenanceInfo> findAllByVehicleIds(List<String> vehicleIds) {
        ResponseEntity<List> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(vehicleIds, null);
            return restClient.findAll("/activityMaintenance/findAllByVehicleIds", requestEntity, new ParameterizedTypeReference<List<ActivityMaintenanceInfo>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:findAllByVehicleIds:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 作废单据
     *
     * @param objId
     */
    public boolean desertTask(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/activityMaintenance/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
