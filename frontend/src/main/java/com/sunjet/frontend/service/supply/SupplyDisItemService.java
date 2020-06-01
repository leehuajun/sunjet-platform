package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyDisItemInfo;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
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
 * 二次分配
 */
@Service("supplyDisItemService")
@Slf4j
public class SupplyDisItemService {

    @Autowired
    private RestClient restClient;


    public SupplyDisItemInfo save(SupplyDisItemItem supplyDisItemInfo) {
        ResponseEntity<SupplyDisItemInfo> responseEntity = null;
        try {
            HttpEntity<SupplyDisItemItem> requestEntity = new HttpEntity<>(supplyDisItemInfo, null);
            responseEntity = restClient.post("/supplyDisItem/save", requestEntity, SupplyDisItemInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyDisItemServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }


    public boolean delete(String ObjId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(ObjId, null);
            responseEntity = restClient.delete("/supplyDisItem/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyDisItemServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }


    public PageResult<SupplyDisItemItem> getPageList(PageParam<SupplyDisItemItem> pageParam) {
        try {
            return restClient.getPageList("/supplyDisItem/getPageList", pageParam, new ParameterizedTypeReference<PageResult<SupplyDisItemItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyDisItemServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找二次分配
     *
     * @param supplyDisItemId
     * @return
     */

    public SupplyDisItemItem findOne(String supplyDisItemId) {
        ResponseEntity<SupplyDisItemItem> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(supplyDisItemId, null);
            responseEntity = restClient.get("/supplyDisItem/findOne", requestEntity, SupplyDisItemItem.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyDisItemServiceImpl:findOne:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过来源单据查二次分配
     *
     * @param srcDocNo
     * @return
     */

    public List<String> finAllObjIdBySrcDocNo(String srcDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("srcDocNo", srcDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyDisItem/finAllObjIdBySrcDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过调拨通知查二次分配
     *
     * @param supplyNoticeDocNo
     * @return
     */

    public List<String> finAllObjIdBySupplyNoticeDocNo(String supplyNoticeDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("supplyNotcieDocNo", supplyNoticeDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyDisItem/finAllObjIdBySupplyNoticeDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过来VIN查二次分配
     *
     * @param vin
     * @return
     */

    public List<String> finAllObjIdByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyDisItem/finAllObjIdByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
