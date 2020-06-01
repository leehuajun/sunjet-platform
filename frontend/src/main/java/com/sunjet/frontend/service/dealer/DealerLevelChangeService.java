package com.sunjet.frontend.service.dealer;

import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestInfo;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestItem;
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
 * 服务站等级变更申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@Service("dealerLevelChangeService")
public class DealerLevelChangeService {

    @Autowired
    private RestClient restClient;


    /**
     * 获取列表集合
     *
     * @return
     */

    public List<DealerLevelChangeRequestInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerLevelChangeRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerLevelChangeRequestItem> getPageList(PageParam<DealerLevelChangeRequestItem> pageParam) {
        try {
            return restClient.getPageList("/dealerLevelChangeRequest/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerLevelChangeRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public DealerLevelChangeRequestInfo findOneById(String objId) {
        ResponseEntity<DealerLevelChangeRequestInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/dealerLevelChangeRequest/findOneById", requestEntity, DealerLevelChangeRequestInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 启动流程
     *
     * @param dealerLevelChangeRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(DealerLevelChangeRequestInfo dealerLevelChangeRequest, ActiveUser activeUser) {
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
        map.put("entity", dealerLevelChangeRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/dealerLevelChangeRequest/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/dealerLevelChangeRequest/deleteByObjId", requestEntity, Boolean.class);
            log.info("DealerLevelChangeServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param dealerLevelChangeRequestInfo
     * @return
     */

    public DealerLevelChangeRequestInfo save(DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo) {
        ResponseEntity<DealerLevelChangeRequestInfo> responseEntity = null;
        try {
            HttpEntity<DealerLevelChangeRequestInfo> requestEntity = new HttpEntity<>(dealerLevelChangeRequestInfo, null);
            responseEntity = restClient.post("/dealerLevelChangeRequest/save", requestEntity, DealerLevelChangeRequestInfo.class);
            log.info("DealerLevelChangeServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param dealerLevelChangeRequestInfo
     * @return
     */

    public boolean delete(DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<DealerLevelChangeRequestInfo> requestEntity = new HttpEntity<>(dealerLevelChangeRequestInfo, null);
            responseEntity = restClient.delete("/dealerLevelChangeRequest/delete", requestEntity, Boolean.class);
            log.info("DealerLevelChangeServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerLevelChangeServiceImpl:delete:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/dealerLevelChangeRequest/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
