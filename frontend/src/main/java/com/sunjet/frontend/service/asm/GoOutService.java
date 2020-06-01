package com.sunjet.frontend.service.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 外出活动列表
 */
@Slf4j
@Service("goOutService")
public class GoOutService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();


    /**
     * 通过三包单objid  获取外出活动列表
     *
     * @return
     */

    public List<GoOutInfo> findAllByWarrantyMaintenanceObjId(String objId) {
        List<GoOutInfo> goOutInfoList = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;
        try {
            if (StringUtils.isNotBlank(objId)) {
                HttpEntity requestEntity = new HttpEntity<>(objId, null);
                return restClient.findAll("/goOuts/findAllByWarrantyMaintenanceObjId", requestEntity, new ParameterizedTypeReference<List<GoOutInfo>>() {
                });

            } else {
                log.info("GoOutServicelmpl:findAllByWarrantyMaintenanceObjId:success");
                return goOutInfoList;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("GoOutServicelmpl:findAllByWarrantyMaintenanceObjId:error:" + e.getMessage());
            return null;
        }

    }


    /**
     * 删除外出
     *
     * @param objId
     * @return
     */

    public boolean deleteByObjId(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/goOuts/delete", requestEntity, Boolean.class);

            log.info("GoOutServicelmpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("GoOutServicelmpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过活动服务单获取外出活动
     *
     * @param objId
     * @return
     */

    public List<GoOutInfo> findAllByActivityMaintenanceObjId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            return restClient.findAll("/goOuts/findAllByActivityMaintenanceObjId", requestEntity, new ParameterizedTypeReference<List<GoOutInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("GoOutServicelmpl:findAllByActivityMaintenanceObjId:error" + e.getMessage());
            return null;
        }
    }


    public List<GoOutInfo> saveList(List<GoOutInfo> goOuts) {
        try {
            HttpEntity<List<GoOutInfo>> requestEntity = new HttpEntity<>(goOuts, null);
            return restClient.post("/goOuts/saveList", requestEntity, new ParameterizedTypeReference<List<GoOutInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
