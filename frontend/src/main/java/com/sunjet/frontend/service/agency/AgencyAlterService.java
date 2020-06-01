package com.sunjet.frontend.service.agency;

import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAlterRequestItem;
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
 * Created by SUNJET_QRY on 2017/8/1.
 * 合作商变更申请
 */
@Slf4j
@Service("agencyAlterService")
public class AgencyAlterService {

    @Autowired
    private RestClient restClient;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    public PageResult<AgencyAlterRequestItem> getPageList(PageParam<AgencyAlterRequestItem> pageParam) {
        try {
            return restClient.getPageList("/agencyAlter/getPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencyAlterRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAlterServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */
    public AgencyAlterRequestInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<AgencyAlterRequestInfo> responseEntity = restClient.get("/agencyAlter/findOne", requestEntity, AgencyAlterRequestInfo.class);
            AgencyAlterRequestInfo agencyAlterRequestInfo = responseEntity.getBody();
            log.info("AgencyAlterServiceImpl:findOne:success");
            return agencyAlterRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAlterServiceImpl:findOne:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存、新增
     *
     * @param agencyAlterRequestInfo
     * @return
     */
    public AgencyAlterRequestInfo save(AgencyAlterRequestInfo agencyAlterRequestInfo) {
        try {
            HttpEntity<AgencyAlterRequestInfo> requestEntity = new HttpEntity<>(agencyAlterRequestInfo, null);
            ResponseEntity<AgencyAlterRequestInfo> ResponseEntity = restClient.post("/agencyAlter/save", requestEntity, AgencyAlterRequestInfo.class);
            log.info("AgencyAlterServiceImpl:save:success");
            return ResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAlterServiceImpl:save:error" + e.getMessage());
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
            HttpEntity<String> request = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/agencyAlter/delete", request, Boolean.class);
            log.info("AgencyAlterServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyAlterServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param agencyAlterRequest
     * @param activeUser
     * @return
     */
    public Map<String, String> startProcess(AgencyAlterRequestInfo agencyAlterRequest, ActiveUser activeUser) {
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
        map.put("entity", agencyAlterRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/agencyAlter/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/agencyAlter/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
