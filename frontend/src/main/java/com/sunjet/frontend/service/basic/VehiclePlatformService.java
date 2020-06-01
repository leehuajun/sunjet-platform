package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-10-18 14:09
 * @description: 说明
 */
@Slf4j
@Service("vehiclePlatformService")
public class VehiclePlatformService {

    @Autowired
    private RestClient restClient;

    public VehiclePlatformInfo save(VehiclePlatformInfo info) {
        try {
            HttpEntity<VehiclePlatformInfo> requestEntity = new HttpEntity<>(info, null);
            ResponseEntity<VehiclePlatformInfo> ResponseEntity = restClient.get("/vehiclePlatform/save", requestEntity, VehiclePlatformInfo.class);
            VehiclePlatformInfo vehiclePlatformInfo = ResponseEntity.getBody();
            log.info("VehiclePlatformService:save:success");
            return vehiclePlatformInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehiclePlatformService:save:error" + e.getMessage());
            return null;
        }
    }

    public boolean delete(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> ResponseEntity = restClient.get("/vehiclePlatform/delete", requestEntity, Boolean.class);
            boolean isOk = ResponseEntity.getBody();
            log.info("VehiclePlatformService:delete:success");
            return isOk;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehiclePlatformService:delete:error" + e.getMessage());
            return false;
        }
    }

    public VehiclePlatformInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<VehiclePlatformInfo> ResponseEntity = restClient.get("/vehiclePlatform/findOne", requestEntity, VehiclePlatformInfo.class);
            VehiclePlatformInfo info = ResponseEntity.getBody();
            log.info("PartService:findOne:success");
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:findOne:error" + e.getMessage());
            return null;
        }
    }

    public List<VehiclePlatformInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<VehiclePlatformInfo> list = restClient.findAll("/vehiclePlatform/findAll", requestEntity, new ParameterizedTypeReference<List<VehiclePlatformInfo>>() {
            });
            log.info("VehiclePlatformService:findAll:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehiclePlatformService:findAll:error:" + e.getMessage());
            return null;
        }
    }
}
