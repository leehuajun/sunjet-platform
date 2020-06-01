package com.sunjet.frontend.service.asm;


import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementItem;
import com.sunjet.dto.asms.settlement.ExpenseItemInfo;
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
@Service("dealerSettlementService")
@Slf4j
public class DealerSettlementService {
    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param dealerSettlementInfo
     * @return
     */

    public DealerSettlementInfo save(DealerSettlementInfo dealerSettlementInfo) {
        ResponseEntity<DealerSettlementInfo> responseEntity = null;
        try {
            HttpEntity<DealerSettlementInfo> requestEntity = new HttpEntity<>(dealerSettlementInfo, null);
            responseEntity = restClient.post("/dealerSettlement/save", requestEntity, DealerSettlementInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerSettlementServiceImpl:save:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/dealerSettlement/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerSettlementServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<DealerSettlementItem> getPageList(PageParam<DealerSettlementItem> pageParam) {
        try {
            return restClient.getPageList("/dealerSettlement/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerSettlementItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerSettlementServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */

    public DealerSettlementInfo findOneById(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<DealerSettlementInfo> responseEntity = restClient.get("/dealerSettlement/findOneById", httpEntity, DealerSettlementInfo.class);
            DealerSettlementInfo dealerSettlementInfo = responseEntity.getBody();

            List<ExpenseItemInfo> expenseItemInfos = findByDealerSettlementId(objId);
            if (expenseItemInfos != null) {
                dealerSettlementInfo.setItems(expenseItemInfos);
            }
            return dealerSettlementInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerSettlementServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过费用id查找费用需求列表
     *
     * @param objId
     * @return
     */

    public List<ExpenseItemInfo> findByDealerSettlementId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<ExpenseItemInfo> list = restClient.findAll("/dealerSettlement/findByDealerSettlementId", requestEntity, new ParameterizedTypeReference<List<ExpenseItemInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过费用删除的同时删除与此关联的费用子行
     *
     * @param objId
     */

    public boolean deleteByDealerSettlementId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/dealerSettlement/deleteByDealerSettlementId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerSettlementServiceImpl:deleteByDealerSettlementId:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param dealerSettlement
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(DealerSettlementInfo dealerSettlement, ActiveUser activeUser) {
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
        map.put("entity", dealerSettlement);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/dealerSettlement/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/dealerSettlement/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
