package com.sunjet.frontend.service.supply;

import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
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

@Service("supplyNoticeService")
@Slf4j
public class SupplyNoticeService {

    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param supplyNoticeInfo
     * @return
     */

    public SupplyNoticeInfo save(SupplyNoticeInfo supplyNoticeInfo) {
        ResponseEntity<SupplyNoticeInfo> responseEntity = null;
        try {
            HttpEntity<SupplyNoticeInfo> requestEntity = new HttpEntity<>(supplyNoticeInfo, null);
            responseEntity = restClient.post("/supplyNotice/save", requestEntity, SupplyNoticeInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:save:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/supplyNotice/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<SupplyNoticeItem> getPageList(PageParam<SupplyNoticeItem> pageParam) {
        try {
            return restClient.getPageList("/supplyNotice/getPageList", pageParam, new ParameterizedTypeReference<PageResult<SupplyNoticeItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过id查一个实体对象
     *
     * @param objId
     * @return
     */

    public SupplyNoticeInfo findByOne(String objId) {
        ResponseEntity<SupplyNoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/supplyNotice/findByOne", requestEntity, SupplyNoticeInfo.class);
            log.info("SupplyNoticeServiceImpl:findByOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findByOne:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过调拨通知单id查一个调拨子行
     *
     * @param supplyNoticeId
     * @return
     */

    public List<SupplyNoticeItemInfo> findByNoticeId(String supplyNoticeId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(supplyNoticeId, null);
            List<SupplyNoticeItemInfo> list = restClient.findAll("/supplyNotice/findByNoticeId", requestEntity, new ParameterizedTypeReference<List<SupplyNoticeItemInfo>>() {
            });
            log.info("SupplyNoticeServiceImpl:findByNoticeId:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findByNoticeId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 启动流程
     *
     * @param supplyNoticeRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(SupplyNoticeInfo supplyNoticeRequest, ActiveUser activeUser) {
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
        map.put("entity", supplyNoticeRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/supplyNotice/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查调拨件收货状态
     *
     * @param supplyNoticeId
     * @return
     */

    public Boolean checkSupplyReceiveState(String supplyNoticeId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(supplyNoticeId, null);
            responseEntity = restClient.get("/supplyNotice/checkSupplyReceiveState", requestEntity, Boolean.class);
            log.info("SupplyNoticeServiceImpl:checkSupplyReceiveState:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:checkSupplyReceiveState:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过来源单号查询供货通知单子行
     *
     * @param srcDocNo
     * @return
     */

    public List<SupplyItemInfo> findSupplyItemIdsBySrcDocNo(String srcDocNo) {
        Map<String, String> docNo = new HashMap<>();
        docNo.put("docNo", srcDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(docNo, null);
            List<SupplyItemInfo> list = restClient.findAll("/supplyNotice/findSupplyItemIdsBySrcDocNo", requestEntity, new ParameterizedTypeReference<List<SupplyItemInfo>>() {
            });
            log.info("SupplyNoticeServiceImpl:findSupplyItemIdsBySrcDocNo:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findSupplyItemIdsBySrcDocNo:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过来Vin查询供货通知单子行
     *
     * @param vin
     * @return
     */

    public List<SupplyItemInfo> findSupplyItemIdsByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<SupplyItemInfo> list = restClient.findAll("/supplyNotice/findSupplyItemIdsByVin", requestEntity, new ParameterizedTypeReference<List<SupplyItemInfo>>() {
            });
            log.info("SupplyNoticeServiceImpl:findSupplyItemIdsByVin:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findSupplyItemIdsByVin:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过活动通知单查询所有调拨通知单objid
     *
     * @param activityNoticeDocNo
     * @return
     */

    public List<String> findAllObjIdByActivityNoticeDocNo(String activityNoticeDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("activityNoticeDocNo", activityNoticeDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyNotice/findAllObjIdByActivityNoticeDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            log.info("SupplyNoticeServiceImpl:findAllObjIdByActivityNoticeDocNo:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findAllObjIdByActivityNoticeDocNo:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过VIN查询所有调拨通知单objid
     *
     * @param vin
     * @return
     */

    public List<String> findSupplyNoticeIdsByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/supplyNotice/findSupplyNoticeIdsByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            log.info("SupplyNoticeServiceImpl:findSupplyNoticeIdsByVin:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SupplyNoticeServiceImpl:findSupplyNoticeIdsByVin:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过单号查询调拨通知单
     *
     * @param supplyNoticeDocNo
     * @return
     */

    public SupplyNoticeInfo findOneByDocNo(String supplyNoticeDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("supplyNoticeDocNo", supplyNoticeDocNo);
        ResponseEntity<SupplyNoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.get("/supplyNotice/findOneByDocNo", requestEntity, SupplyNoticeInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return responseEntity.getBody();
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
            responseEntity = restClient.delete("/supplyNotice/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 通过来源id查询调拨供货单
     *
     * @param objId
     * @return
     */
    public SupplyNoticeInfo findOneBySrcDocId(String objId) {
        ResponseEntity<SupplyNoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/supplyNotice/findOneBySrcDocId", requestEntity, SupplyNoticeInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return responseEntity.getBody();
        }
    }
}
