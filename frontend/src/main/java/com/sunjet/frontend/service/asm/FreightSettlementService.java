package com.sunjet.frontend.service.asm;


import com.sunjet.dto.asms.settlement.FreightExpenseInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementItem;
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
 * 运费结算单
 */
@Service("freightSettlementService")
@Slf4j
public class FreightSettlementService {
    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param freightSettlementInfo
     * @return
     */

    public FreightSettlementInfo save(FreightSettlementInfo freightSettlementInfo) {
        ResponseEntity<FreightSettlementInfo> responseEntity = null;
        try {
            HttpEntity<FreightSettlementInfo> requestEntity = new HttpEntity<>(freightSettlementInfo, null);
            responseEntity = restClient.post("/freightSettlement/save", requestEntity, FreightSettlementInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:save:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/freightSettlement/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<FreightSettlementItem> getPageList(PageParam<FreightSettlementItem> pageParam) {
        try {
            return restClient.getPageList("/freightSettlement/getPageList", pageParam, new ParameterizedTypeReference<PageResult<FreightSettlementItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */

    public FreightSettlementInfo findOneById(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<FreightSettlementInfo> responseEntity = restClient.get("/freightSettlement/findOneById", httpEntity, FreightSettlementInfo.class);
            List<FreightExpenseInfo> freightExpenseInfos = findByFreightSettlementId(objId);
            FreightSettlementInfo freightSettlementInfo = responseEntity.getBody();
            if (freightExpenseInfos != null) {
                freightSettlementInfo.setFreightExpenseInfos(freightExpenseInfos);
            }

            return freightSettlementInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过运费结算id查找运费子行列表
     *
     * @param objId
     * @return
     */

    public List<FreightExpenseInfo> findByFreightSettlementId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<FreightExpenseInfo> list = restClient.findAll("/freightSettlement/findByFreightSettlementId", requestEntity, new ParameterizedTypeReference<List<FreightExpenseInfo>>() {
            });
            log.info("FreightSettlementServiceImpl:findByFreightSettlementId:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:findByFreightSettlementId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除配件运费结算的同时把运费结算子行一同删除
     *
     * @param objId
     */

    public boolean deleteByFreightSettlementId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/freightSettlement/deleteByFreightSettlementId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FreightSettlementServiceImpl:deleteByFreightSettlementId:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param freightSettlement
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(FreightSettlementInfo freightSettlement, ActiveUser activeUser) {
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
        map.put("entity", freightSettlement);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/freightSettlement/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
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
            responseEntity = restClient.delete("/freightSettlement/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
