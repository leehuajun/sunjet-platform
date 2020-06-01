package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
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
import java.util.List;
import java.util.Map;

/**
 * 调拨供货单
 */
@Service("supplyService")
@Slf4j
public class SupplyService {
    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param supplyInfo
     * @return
     */

    public SupplyInfo save(SupplyInfo supplyInfo) {
        ResponseEntity<SupplyInfo> responseEntity = null;
        try {
            HttpEntity<SupplyInfo> requestEntity = new HttpEntity<>(supplyInfo, null);
            responseEntity = restClient.post("/supply/save", requestEntity, SupplyInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/supply/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<SupplyItem> getPageList(PageParam<SupplyItem> pageParam) {
        try {
            return restClient.getPageList("/supply/getPageList", pageParam, new ParameterizedTypeReference<PageResult<SupplyItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查询一个对象实体
     *
     * @param objId
     * @return
     */

    public SupplyInfo findSupplyWithPartsById(String objId) {
        ResponseEntity<SupplyInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/supply/findSupplyWithPartsById", requestEntity, SupplyInfo.class);
            log.info("SupplyServiceImpl:findSupplyWithPartsById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:findSupplyWithPartsById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据供货单id查供货单子行
     *
     * @param supplyId
     * @return
     */

    public List<SupplyItemInfo> findBySupplyId(String supplyId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(supplyId, null);
            List<SupplyItemInfo> list = restClient.findAll("/supply/findBySupplyId", requestEntity, new ParameterizedTypeReference<List<SupplyItemInfo>>() {
            });
            log.info("SupplyServiceImpl:findBySupplyId:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:findBySupplyId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除调拨供货子行列表
     *
     * @param supplyItemInfo
     * @return
     */

    public boolean delete(SupplyItemInfo supplyItemInfo) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<SupplyItemInfo> requestEntity = new HttpEntity<>(supplyItemInfo, null);
            responseEntity = restClient.delete("/supply/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param supplyRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(SupplyInfo supplyRequest, ActiveUser activeUser) {
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
        map.put("entity", supplyRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/supply/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
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
            responseEntity = restClient.delete("/supply/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
