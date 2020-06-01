package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.VehicleModelInfo;
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
@Service("vehicleModelService")
public class VehicleModelService {

    @Autowired
    private RestClient restClient;

    public VehicleModelInfo save(VehicleModelInfo info) {
        try {
            HttpEntity<VehicleModelInfo> requestEntity = new HttpEntity<>(info, null);
            ResponseEntity<VehicleModelInfo> ResponseEntity = restClient.get("/vehicleModel/save", requestEntity, VehicleModelInfo.class);
            VehicleModelInfo vehicleModelInfo = ResponseEntity.getBody();
            log.info("VehicleModelService:save:success");
            return vehicleModelInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleModelService:save:error" + e.getMessage());
            return null;
        }
    }

    public boolean delete(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> ResponseEntity = restClient.get("/vehicleModel/delete", requestEntity, Boolean.class);
            boolean isOk = ResponseEntity.getBody();
            log.info("VehicleModelService:delete:success");
            return isOk;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleModelService:delete:error" + e.getMessage());
            return false;
        }
    }

    public VehicleModelInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<VehicleModelInfo> ResponseEntity = restClient.get("/vehicleModel/findOne", requestEntity, VehicleModelInfo.class);
            VehicleModelInfo info = ResponseEntity.getBody();
            log.info("VehicleModelService:findOne:success");
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleModelService:findOne:error" + e.getMessage());
            return null;
        }
    }

    public List<VehicleModelInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<VehicleModelInfo> list = restClient.findAll("/vehicleModel/findAll", requestEntity, new ParameterizedTypeReference<List<VehicleModelInfo>>() {
            });
            log.info("VehicleModelService:findAll:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleModelService:findAll:error:" + e.getMessage());
            return null;
        }
    }
}
