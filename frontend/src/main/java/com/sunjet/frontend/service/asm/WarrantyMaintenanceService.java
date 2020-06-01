package com.sunjet.frontend.service.asm;

import com.sunjet.dto.asms.asm.*;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.basic.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/1.
 * 三包服务单
 */
@Slf4j
@Service("warrantyMaintenanceService")
public class WarrantyMaintenanceService {


    @Autowired
    private RestClient restClient;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private GoOutService goOutService;
    @Autowired
    private WarrantyMaintainService warrantyMaintainService;
    @Autowired
    private CommissionPartService commissionPartService;



    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<WarrantyMaintenanceItem> getPageList(PageParam<WarrantyMaintenanceItem> pageParam) {
        try {
            log.info("WarrantymaintenanceServicelmpl:getPageList:success");
            return restClient.getPageList("/warrantyMaintenance/getPageList", pageParam, new ParameterizedTypeReference<PageResult<WarrantyMaintenanceItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objid 查实体
     *
     * @param objId
     * @return
     */

    public WarrantyMaintenanceInfo findOneById(String objId) {
        ResponseEntity<WarrantyMaintenanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/warrantyMaintenance/findOneById", requestEntity, WarrantyMaintenanceInfo.class);
            WarrantyMaintenanceInfo warrantyMaintenanceRequest = responseEntity.getBody();
            //加载车辆
            if (warrantyMaintenanceRequest.getVehicleId() != null) {
                warrantyMaintenanceRequest.setVehicleInfo(vehicleService.findOne(warrantyMaintenanceRequest.getVehicleId()));
            } else {
                warrantyMaintenanceRequest.setVehicleInfo(new VehicleInfo());
            }
            //加载维修配件
            warrantyMaintenanceRequest.setCommissionParts(commissionPartService.findAllByWarrantyMaintenanceObjId(warrantyMaintenanceRequest.getObjId()));
            //加载三包维修项目
            warrantyMaintenanceRequest.setWarrantyMaintains(warrantyMaintainService.findAllByWarrantyMaintenanceObjId(warrantyMaintenanceRequest.getObjId()));
            //加载外出列表
            warrantyMaintenanceRequest.setGoOuts(goOutService.findAllByWarrantyMaintenanceObjId(warrantyMaintenanceRequest.getObjId()));
            return warrantyMaintenanceRequest;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:findOneById:error:" + e.getMessage());
            return null;
        }


    }


    /**
     * 保存三包单
     *
     * @param warrantyMaintenanceRequest
     */

    public WarrantyMaintenanceInfo save(WarrantyMaintenanceInfo warrantyMaintenanceRequest) {
        ResponseEntity<WarrantyMaintenanceInfo> responseEntity = null;
        try {
            HttpEntity<WarrantyMaintenanceInfo> requestEntity = new HttpEntity<>(warrantyMaintenanceRequest, null);
            responseEntity = restClient.post("/warrantyMaintenance/save", requestEntity, WarrantyMaintenanceInfo.class);
            WarrantyMaintenanceInfo warrantyMaintenanceInfo = responseEntity.getBody();
            //保存配件需求列表
            List<CommissionPartInfo> commissionParts = warrantyMaintenanceRequest.getCommissionParts();
            for (CommissionPartInfo commissionPart : commissionParts) {
                commissionPart.setWarrantyMaintenance(warrantyMaintenanceInfo.getObjId());
            }
            warrantyMaintenanceInfo.setCommissionParts(commissionPartService.saveList(commissionParts));

            ////保存维修项目
            List<WarrantyMaintainInfo> warrantyMaintains = warrantyMaintenanceRequest.getWarrantyMaintains();
            for (WarrantyMaintainInfo warrantyMaintain : warrantyMaintains) {
                warrantyMaintain.setWarrantyMaintenance(warrantyMaintenanceInfo.getObjId());
            }
            warrantyMaintenanceInfo.setWarrantyMaintains(warrantyMaintainService.saveList(warrantyMaintains));
            //保存外出信息
            List<GoOutInfo> goOuts = warrantyMaintenanceRequest.getGoOuts();
            for (GoOutInfo goOut : goOuts) {
                goOut.setWarrantyMaintenance(warrantyMaintenanceInfo.getObjId());
            }
            warrantyMaintenanceInfo.setGoOuts(goOutService.saveList(goOuts));

            log.info("WarrantymaintenanceServicelmpl:save:success");
            return warrantyMaintenanceInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:save:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 通过单据类型srcDocNo查找三包单里的信息
     *
     * @param srcDocNo
     * @return
     */

    public WarrantyMaintenanceInfo findOneWithOthersBySrcDocNo(String srcDocNo) {
        ResponseEntity<WarrantyMaintenanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(srcDocNo, null);
            responseEntity = restClient.get("/warrantyMaintenance/findOneWithOthersBySrcDocNo", requestEntity, WarrantyMaintenanceInfo.class);
            log.info("WarrantymaintenanceServicelmpl:findOneWithOthersBySrcDocNo:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:findOneWithOthersBySrcDocNo:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 提交流程
     *
     * @param warrantyMaintenanceRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(WarrantyMaintenanceInfo warrantyMaintenanceRequest, ActiveUser activeUser) {
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
        map.put("entity", warrantyMaintenanceRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/warrantyMaintenance/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId删除一个对象
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/warrantyMaintenance/delete", httpEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("warrantyMaintenance:delete:error:" + e.getMessage());
            return false;
        }
    }


    public List<WarrantyMaintenanceInfo> findAllByVehicleId(String vehicleId) {
        ResponseEntity<List> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(vehicleId, null);
            return restClient.findAll("/warrantyMaintenance/findAllByVehicleId", requestEntity, new ParameterizedTypeReference<List<WarrantyMaintenanceInfo>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantymaintenanceServicelmpl:findAllByVehicleId:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/warrantyMaintenance/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}