package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
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
 * 待发货清单
 */
@Service("supplyWaitingItemService")
@Slf4j
public class SupplyWaitingItemService {

    @Autowired
    private RestClient restClient;

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<SupplyWaitingItemItem> getPageList(PageParam<SupplyWaitingItemItem> pageParam) {
        try {
            return restClient.getPageList("/supplyWaitingItem/getPageList", pageParam, new ParameterizedTypeReference<PageResult<SupplyWaitingItemItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyWaitingItemServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存实体
     *
     * @param supplyWaitingItemItem
     * @return
     */

    public SupplyWaitingItemItem save(SupplyWaitingItemItem supplyWaitingItemItem) {
        ResponseEntity<SupplyWaitingItemItem> responseEntity = null;
        try {
            HttpEntity<SupplyWaitingItemItem> requestEntity = new HttpEntity<>(supplyWaitingItemItem, null);
            responseEntity = restClient.post("/supplyWaitingItem/save", requestEntity, SupplyWaitingItemItem.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyWaitingItemServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId查找 待返回清单
     *
     * @param objId
     * @return
     */

    public SupplyWaitingItemItem findSupplyWaitingItemById(String objId) {
        ResponseEntity<SupplyWaitingItemItem> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/supplyWaitingItem/findSupplyWaitingItemById", requestEntity, SupplyWaitingItemItem.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除待发货清单
     *
     * @param objId
     * @return
     */

    public Boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/supplyWaitingItem/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过来源单号查询待发货objIds
     *
     * @param srcDocNo
     * @return
     */

    public List<String> findAllObjIdsBySrcDocNo(String srcDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("srcDocNo", srcDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyWaitingItem/findAllObjIdsBySrcDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过来vin查询待发货objIds
     *
     * @param vin
     * @return
     */

    public List<String> findAllObjIdsByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyWaitingItem/findAllObjIdsByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过调拨通知单号查询待发货objIds
     *
     * @param supplyNoticeDocNo
     * @return
     */

    public List<String> findAllObjIdsBySupplyNoticeDocNo(String supplyNoticeDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("supplyNoticeDocNo", supplyNoticeDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyWaitingItem/findAllObjIdsBySupplyNoticeDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SupplyWaitingItemItem> findAllPartByAgency(String agencyCode, String partName, String dealerCode) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("agencyCode", agencyCode);
            map.put("dealerCode", dealerCode);
            map.put("partName", partName);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<SupplyWaitingItemItem> list = restClient.findAll("/supplyWaitingItem/findAllPartByAgency", requestEntity, new ParameterizedTypeReference<List<SupplyWaitingItemItem>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<SupplyWaitingItemItem> findAllByAgencyCode(String agencyCode) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("agencyCode", agencyCode);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<SupplyWaitingItemItem> list = restClient.findAll("/supplyWaitingItem/findAllByAgencyCode", requestEntity, new ParameterizedTypeReference<List<SupplyWaitingItemItem>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
