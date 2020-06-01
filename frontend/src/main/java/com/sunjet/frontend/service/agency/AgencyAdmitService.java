package com.sunjet.frontend.service.agency;

import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestItem;
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
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/7/28.
 * 合作商准入申请
 */
@Slf4j
@Service("agencyAdmitService")
public class AgencyAdmitService {

    @Autowired
    private RestClient restClient;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    public PageResult<AgencyAdmitRequestItem> getPageList(PageParam<AgencyAdmitRequestItem> pageParam) {
        try {
            log.info("AgencyAdmitServiceImpl:getPageList:success");
            return restClient.getPageList("/agencyAdmit/getPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencyAdmitRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAdmitServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */
    public AgencyAdmitRequestInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<AgencyAdmitRequestInfo> responseEntity = restClient.get("/agencyAdmit/findOne", requestEntity, AgencyAdmitRequestInfo.class);
            AgencyAdmitRequestInfo agencyAdmitRequestInfo = responseEntity.getBody();
            log.info("AgencyAdmitServiceImpl:findOne:success");
            return agencyAdmitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAdmitServiceImpl:findOne:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存实体对象
     *
     * @param agencyAdmitRequestInfo
     * @return
     */
    public AgencyAdmitRequestInfo save(AgencyAdmitRequestInfo agencyAdmitRequestInfo) {
        try {
            HttpEntity<AgencyAdmitRequestInfo> requestEntity = new HttpEntity<>(agencyAdmitRequestInfo, null);
            ResponseEntity<AgencyAdmitRequestInfo> ResponseEntity = restClient.post("/agencyAdmit/save", requestEntity, AgencyAdmitRequestInfo.class);
            log.info("AgencyAdmitServiceImpl:save:success");
            return ResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAdmitServiceImpl:save:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除实体
     *
     * @param objId
     * @return
     */
    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> request = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/agencyAdmit/delete", request, Boolean.class);
            log.info("AgencyAdmitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAdmitServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param agencyAdmitRequestInfo
     * @param activeUser
     * @return
     */
    public Map<String, String> startProcess(AgencyAdmitRequestInfo agencyAdmitRequestInfo, ActiveUser activeUser) {
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
        map.put("entity", agencyAdmitRequestInfo);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/agencyAdmit/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/agencyAdmit/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
