package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调拨供货子行
 */
@Service("supplyItemService")
@Slf4j
public class SupplyItemService {

    @Autowired
    private RestClient restClient;


    /**
     * 通过调拨供货单父表objId删除
     *
     * @param objId
     * @return
     */

    public Boolean deleteBySupplyObjId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/supplyItem/deleteBySupplyObjId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyItemServiceImpl:deleteBySupplyObjId:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过调拨供货子行删除
     *
     * @param objId
     * @return
     */

    public Boolean deleteByObjId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/supplyItem/deleteByObjId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyItemServiceImpl:deleteByObjid:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过调拨通知单明细obji 获取供货单明细
     *
     * @param objId
     * @return
     */

    public List<SupplyItemInfo> findAllByNoticeItemId(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            List<SupplyItemInfo> all = restClient.findAll("/supplyItem/findAllByNoticeItemId", requestEntity, new ParameterizedTypeReference<List<SupplyItemInfo>>() {
            });
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyItemServiceImpl:findAllByNoticeItemId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个供货单配件
     *
     * @param objId
     * @return
     */
    public SupplyItemInfo findOneByID(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<SupplyItemInfo> responseEntity = restClient.post("/supplyItem/findOneByID", httpEntity, SupplyItemInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
