package com.sunjet.frontend.service.agency;

import com.sunjet.dto.asms.agency.AgencyQuitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyQuitRequestItem;
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
 * 合作商退出申请
 */
@Slf4j
@Service("agencyQuitService")
public class AgencyQuitService {

    @Autowired
    private RestClient restClient;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    public PageResult<AgencyQuitRequestItem> getPageList(PageParam<AgencyQuitRequestItem> pageParam) {
        try {
            return restClient.getPageList("/agencyQuit/getPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencyQuitRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyQuitServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */
    public AgencyQuitRequestInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<AgencyQuitRequestInfo> responseEntity = restClient.get("/agencyQuit/findOne", requestEntity, AgencyQuitRequestInfo.class);
            AgencyQuitRequestInfo agencyQuitRequestInfo = responseEntity.getBody();
            log.info("AgencyQuitServiceImpl:findOne:success");
            return agencyQuitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyQuitServiceImpl:findOne:error:" + e.getMessage());
            return null;
        }
    }

    public AgencyQuitRequestInfo save(AgencyQuitRequestInfo agencyQuitRequestInfo) {
        try {
            HttpEntity<AgencyQuitRequestInfo> requestEntity = new HttpEntity<>(agencyQuitRequestInfo, null);
            ResponseEntity<AgencyQuitRequestInfo> ResponseEntity = restClient.post("/agencyQuit/save", requestEntity, AgencyQuitRequestInfo.class);
            log.info("AgencyQuitServiceImpl:save:success");
            return ResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyQuitServiceImpl:save:error" + e.getMessage());
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
            responseEntity = restClient.delete("/agencyQuit/delete", request, Boolean.class);
            log.info("AgencyQuitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyQuitServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    public Map<String, String> startProcess(AgencyQuitRequestInfo agencyQuitRequest, ActiveUser activeUser) {
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
        map.put("entity", agencyQuitRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/agencyQuit/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/agencyQuit/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
