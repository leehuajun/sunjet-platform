package com.sunjet.frontend.service.asm;

import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wfb on 17-8-1.
 * 首保服务单
 */
@Service("firstMaintenanceService")
@Slf4j
public class FirstMaintenanceService {


    @Autowired
    private RestClient restClient;

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<FirstMaintenanceItem> getPageList(PageParam<FirstMaintenanceItem> pageParam) {
        try {
            return restClient.getPageList("/firstMaintenance/getPageList", pageParam, new ParameterizedTypeReference<PageResult<FirstMaintenanceItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FirstMaintenanceServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * find one by obj id
     *
     * @param businessId
     * @return
     */

    public FirstMaintenanceInfo findOneWithGoOutsById(String businessId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(businessId, null);
            ResponseEntity<FirstMaintenanceInfo> responseEntity = restClient.get("/firstMaintenance/findOneWithGoOutsById", httpEntity, FirstMaintenanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FirstMaintenanceServiceImpl:findOneWithGoOutsById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存
     *
     * @param firstMaintenanceInfo
     * @return
     */

    public FirstMaintenanceInfo save(FirstMaintenanceInfo firstMaintenanceInfo) {
        try {
            HttpEntity<FirstMaintenanceInfo> httpEntity = new HttpEntity<>(firstMaintenanceInfo, null);
            ResponseEntity<FirstMaintenanceInfo> responseEntity = restClient.post("/firstMaintenance/save", httpEntity, FirstMaintenanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FirstMaintenanceServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除实体
     *
     * @param objId
     * @return
     */

    public Boolean delete(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/firstMaintenance/delete", httpEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FirstMaintenanceServiceImpl:delete:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 启动流程
     *
     * @param firstMaintenanceRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(FirstMaintenanceInfo firstMaintenanceRequest, ActiveUser activeUser) {
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
        map.put("entity", firstMaintenanceRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/firstMaintenance/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * find one by vehicleId
     *
     * @param vehicleId
     * @return
     */

    public FirstMaintenanceInfo findOneByVehicleId(String vehicleId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(vehicleId, null);
            ResponseEntity<FirstMaintenanceInfo> responseEntity = restClient.get("/firstMaintenance/findOneByVehicleId", httpEntity, FirstMaintenanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FirstMaintenanceServiceImpl:findOneByVehicleId:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 作废单据
     *
     * @return
     */
    public boolean desertTask(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/firstMaintenance/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
