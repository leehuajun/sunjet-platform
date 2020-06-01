package com.sunjet.frontend.service.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.basic.DealerService;
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
 * 服务站准入申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@Service("dealerAdmitService")
public class DealerAdmitService {

    @Autowired
    private RestClient restClient;
    @Autowired
    private DealerService dealerService;


    /**
     * 获取列表集合
     *
     * @return
     */

    public List<DealerAdmitRequestInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerAdmitRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerAdmitRequestItem> getPageList(PageParam<DealerAdmitRequestItem> pageParam) {
        try {
            return restClient.getPageList("/dealerAdmitRequest/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerAdmitRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public DealerAdmitRequestInfo findOneById(String objId) {
        ResponseEntity<DealerAdmitRequestInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/dealerAdmitRequest/findOneById", requestEntity, DealerAdmitRequestInfo.class);
            DealerAdmitRequestInfo dealerAdmitRequestInfo = responseEntity.getBody();
            DealerInfo dealerInfo = dealerService.findOneById(dealerAdmitRequestInfo.getDealer());
            dealerAdmitRequestInfo.setDealerInfo(dealerInfo);
            return dealerAdmitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 启动流程
     *
     * @param dealerAdmitRequestInfo
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(DealerAdmitRequestInfo dealerAdmitRequestInfo, ActiveUser activeUser) {
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
        map.put("entity", dealerAdmitRequestInfo);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/dealerAdmitRequest/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId删除
     *
     * @param objId
     */

    public boolean deleteByObjId(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/dealerAdmitRequest/deleteByObjId", requestEntity, Boolean.class);
            log.info("DealerAdmitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param dealerAdmitRequestInfo
     * @return
     */

    public DealerAdmitRequestInfo save(DealerAdmitRequestInfo dealerAdmitRequestInfo) {
        ResponseEntity<DealerAdmitRequestInfo> responseEntity = null;
        try {
            HttpEntity<DealerAdmitRequestInfo> requestEntity = new HttpEntity<>(dealerAdmitRequestInfo, null);
            responseEntity = restClient.post("/dealerAdmitRequest/save", requestEntity, DealerAdmitRequestInfo.class);
            log.info("DealerAdmitServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param dealerAdmitRequestInfo
     * @return
     */

    public boolean delete(DealerAdmitRequestInfo dealerAdmitRequestInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<DealerAdmitRequestInfo> requestEntity = new HttpEntity<>(dealerAdmitRequestInfo, null);
            responseEntity = restClient.delete("/dealerAdmitRequest/delete", requestEntity, Boolean.class);
            log.info("DealerAdmitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAdmitServiceImpl:delete:error:" + e.getMessage());
            return false;
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
            responseEntity = restClient.delete("/dealerAdmitRequest/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
