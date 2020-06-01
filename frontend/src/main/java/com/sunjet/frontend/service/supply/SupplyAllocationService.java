package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
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
 * 调拨分配
 */
@Service("supplyAllocationService")
@Slf4j
public class SupplyAllocationService {

    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param supplyNoticeItemInfo
     * @return
     */
    public SupplyNoticeItemInfo save(SupplyAllocationItem supplyNoticeItemInfo) {

        ResponseEntity<SupplyNoticeItemInfo> responseEntity = null;
        try {
            HttpEntity<SupplyAllocationItem> requestEntity = new HttpEntity<>(supplyNoticeItemInfo, null);
            responseEntity = restClient.post("/supplyAllocation/save", requestEntity, SupplyNoticeItemInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyAllocationServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    public boolean delete(String ObjId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(ObjId, null);
            responseEntity = restClient.delete("/supplyAllocation/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyAllocationServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyAllocationItem> getPageList(PageParam<SupplyAllocationItem> pageParam) {
        try {
            return restClient.getPageList("/supplyAllocation/getPageList", pageParam, new ParameterizedTypeReference<PageResult<SupplyAllocationItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyAllocationServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查一个实体
     *
     * @param objId
     * @return
     */

    public SupplyAllocationItem findOne(String objId) {
        ResponseEntity<SupplyAllocationItem> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/supplyAllocation/findOne", requestEntity, SupplyAllocationItem.class);
            log.info("SupplyAllocationServiceImpl:findOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyAllocationServiceImpl:findOne:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过VIN查询调拨分配单明细objId
     *
     * @param vin
     * @return
     */

    public List<String> findAllObjIdByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyAllocation/findAllObjIdByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
