package com.sunjet.frontend.service.asm;

import com.sunjet.dto.asms.settlement.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待结算清单（配件／服务／运费）
 */
@Service("pendingSettlementDetailsService")
@Slf4j
public class PendingSettlementDetailsService {

    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param pendingSettlementDetailInfo
     * @return
     */

    public PendingSettlementDetailInfo save(PendingSettlementDetailInfo pendingSettlementDetailInfo) {
        ResponseEntity<PendingSettlementDetailInfo> responseEntity = null;
        try {
            HttpEntity<PendingSettlementDetailInfo> requestEntity = new HttpEntity<>(pendingSettlementDetailInfo, null);
            responseEntity = restClient.post("/pendingSettlementDetails/save", requestEntity, PendingSettlementDetailInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */

    public boolean delete(String ObjId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(ObjId, null);
            responseEntity = restClient.delete("/pendingSettlementDetails/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @return
     */

    public PageResult<PendingSettlementDetailItems> getPageList(PageParam<PendingSettlementDetailItems> pageParam) {
        try {
            return restClient.getPageList("/pendingSettlementDetails/getPageList", pageParam, new ParameterizedTypeReference<PageResult<PendingSettlementDetailItems>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据单据编号获取一个实体对象
     *
     * @param srcDocID
     * @return
     */

    public PendingSettlementDetailInfo getOneBySrcDocID(String srcDocID) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(srcDocID, null);
            ResponseEntity<PendingSettlementDetailInfo> responseEntity = restClient.get("/pendingSettlementDetails/getOneBySrcDocID", httpEntity, PendingSettlementDetailInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getOneBySrcDocID:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据结算单ID 获取待结算单列表
     *
     * @param objId
     * @return
     */

    public List<PendingSettlementDetailInfo> getPendingsBySettlementID(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<PendingSettlementDetailInfo> list = restClient.findAll("/pendingSettlementDetails/getPendingsBySettlementID", requestEntity, new ParameterizedTypeReference<List<PendingSettlementDetailInfo>>() {
            });
            log.info("PendingSettlementDetailsServiceImpl:getPendingsBySettlementID:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getPendingsBySettlementID:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取服务结算单
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */

    public List<PendingSettlementDetailInfo> getDealerSelttlements(String code, Date startDate, Date endDate) {
        try {

            DealerSettlementInfo dealerSettlementInfo = new DealerSettlementInfo();
            dealerSettlementInfo.setDealerCode(code);
            dealerSettlementInfo.setStartDate(startDate);
            dealerSettlementInfo.setEndDate(endDate);
            Map<String, DealerSettlementInfo> map = new HashMap<>();
            map.put("dealerSettlementInfo", dealerSettlementInfo);

            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<PendingSettlementDetailInfo> list = restClient.findAll("/pendingSettlementDetails/getDealerSelttlements", requestEntity, new ParameterizedTypeReference<List<PendingSettlementDetailInfo>>() {
            });
            log.info("PendingSettlementDetailsServiceImpl:getDealerSelttlements:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getDealerSelttlements:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取运费结算单
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */

    public List<PendingSettlementDetailInfo> getFreightSelttlements(String code, Date startDate, Date endDate) {
        try {

            FreightExpenseInfo freightExpenseInfo = new FreightExpenseInfo();
            freightExpenseInfo.setDealerCode(code);
            freightExpenseInfo.setStartDate(startDate);
            freightExpenseInfo.setEndDate(endDate);
            Map<String, FreightExpenseInfo> map = new HashMap<>();
            map.put("freightExpenseInfo", freightExpenseInfo);

            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<PendingSettlementDetailInfo> list = restClient.findAll("/pendingSettlementDetails/getFreightSelttlements", requestEntity, new ParameterizedTypeReference<List<PendingSettlementDetailInfo>>() {
            });
            log.info("PendingSettlementDetailsServiceImpl:getFreightSelttlements:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getFreightSelttlements:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取配件运费单
     *
     * @param agencyCode
     * @param startDate
     * @param endDate
     * @return
     */

    public List<PendingSettlementDetailInfo> getAgencySelttlements(String agencyCode, Date startDate, Date endDate) {

        try {

            PartExpenseItemsInfo partExpenseItemsInfo = new PartExpenseItemsInfo();
//            partExpenseItemsInfo.setDealerCode(agencyCode);
            partExpenseItemsInfo.setAgencyCode(agencyCode);
            partExpenseItemsInfo.setStartDate(startDate);
            partExpenseItemsInfo.setEndDate(endDate);
            Map<String, PartExpenseItemsInfo> map = new HashMap<>();
            map.put("partExpenseItemsInfo", partExpenseItemsInfo);

            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<PendingSettlementDetailInfo> list = restClient.findAll("/pendingSettlementDetails/getAgencySelttlements", requestEntity, new ParameterizedTypeReference<List<PendingSettlementDetailInfo>>() {
            });
            log.info("PendingSettlementDetailsServiceImpl:getAgencySelttlements:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:getAgencySelttlements:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查一个vo实体
     *
     * @param objId
     * @return
     */

    public PendingSettlementDetailInfo findOneById(String objId) {
        ResponseEntity<PendingSettlementDetailInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/pendingSettlementDetails/findOneById", requestEntity, PendingSettlementDetailInfo.class);
            log.info("PendingSettlementDetailsServiceImpl:findOneById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PendingSettlementDetailsServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }


}
