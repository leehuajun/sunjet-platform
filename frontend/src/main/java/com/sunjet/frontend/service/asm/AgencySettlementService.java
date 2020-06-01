package com.sunjet.frontend.service.asm;


import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementItem;
import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;
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
 * 配件结算单
 */
@Service("agencySettlementService")
@Slf4j
public class AgencySettlementService {
    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param agencySettlementInfo
     * @return
     */

    public AgencySettlementInfo save(AgencySettlementInfo agencySettlementInfo) {
        ResponseEntity<AgencySettlementInfo> responseEntity = null;
        try {
            HttpEntity<AgencySettlementInfo> requestEntity = new HttpEntity<>(agencySettlementInfo, null);
            responseEntity = restClient.post("/agencySettlement/save", requestEntity, AgencySettlementInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:save:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/agencySettlement/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<AgencySettlementItem> getPageList(PageParam<AgencySettlementItem> pageParam) {
        try {
            return restClient.getPageList("/agencySettlement/getPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencySettlementItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */

    public AgencySettlementInfo findOneById(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<AgencySettlementInfo> responseEntity = restClient.get("/agencySettlement/findOneById", httpEntity, AgencySettlementInfo.class);
            AgencySettlementInfo agencySettlementInfo = responseEntity.getBody();

            List<PartExpenseItemsInfo> partExpenseItemsInfos = findByAgencySettlementId(objId);
            if (partExpenseItemsInfos != null) {
                agencySettlementInfo.setPartExpenseItemsInfos(partExpenseItemsInfos);
            }


            return agencySettlementInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过配件结算单id查找配件费用子行列表
     *
     * @param objId
     * @return
     */

    public List<PartExpenseItemsInfo> findByAgencySettlementId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<PartExpenseItemsInfo> list = restClient.findAll("/agencySettlement/findByAgencySettlementId", requestEntity, new ParameterizedTypeReference<List<PartExpenseItemsInfo>>() {
            });
            log.info("AgencySettlementServiceImpl:findByAgencySettlementId:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:findByAgencySettlementId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过配件结算单id删除同时配件结算子行
     *
     * @param objId
     */

    public boolean deleteByAgencySettlementId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/agencySettlement/deleteByAgencySettlementId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencySettlementServiceImpl:deleteByAgencySettlementId:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 合作商结算启动流程
     *
     * @param agencySettlement
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(AgencySettlementInfo agencySettlement, ActiveUser activeUser) {
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
        map.put("entity", agencySettlement);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/agencySettlement/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/agencySettlement/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
