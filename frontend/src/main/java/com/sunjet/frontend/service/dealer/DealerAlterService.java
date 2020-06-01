package com.sunjet.frontend.service.dealer;

import com.sunjet.dto.asms.dealer.DealerAlterRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAlterRequestItem;
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
 * 服务站变更申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@Service("dealerAlterService")
public class DealerAlterService {

    @Autowired
    private RestClient restClient;


    /**
     * 获取列表集合
     *
     * @return
     */

    public List<DealerAlterRequestInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerAlterRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerAlterRequestItem> getPageList(PageParam<DealerAlterRequestItem> pageParam) {
        try {
            return restClient.getPageList("/dealerAlterRequest/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerAlterRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public DealerAlterRequestInfo findOneById(String objId) {
        ResponseEntity<DealerAlterRequestInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/dealerAlterRequest/findOneById", requestEntity, DealerAlterRequestInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 启动流程
     *
     * @param dealerAlterRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(DealerAlterRequestInfo dealerAlterRequest, ActiveUser activeUser) {
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
        map.put("entity", dealerAlterRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/dealerAlterRequest/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/dealerAlterRequest/deleteByObjId", requestEntity, Boolean.class);
            log.info("DealerAlterServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param dealerAlterRequestInfo
     * @return
     */

    public DealerAlterRequestInfo save(DealerAlterRequestInfo dealerAlterRequestInfo) {
        ResponseEntity<DealerAlterRequestInfo> responseEntity = null;
        try {
            HttpEntity<DealerAlterRequestInfo> requestEntity = new HttpEntity<>(dealerAlterRequestInfo, null);
            responseEntity = restClient.post("/dealerAlterRequest/save", requestEntity, DealerAlterRequestInfo.class);
            log.info("DealerAlterServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param dealerAlterRequestInfo
     * @return
     */

    public boolean delete(DealerAlterRequestInfo dealerAlterRequestInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<DealerAlterRequestInfo> requestEntity = new HttpEntity<>(dealerAlterRequestInfo, null);
            responseEntity = restClient.delete("/dealerAlterRequest/delete", requestEntity, Boolean.class);
            log.info("DealerAlterServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerAlterServiceImpl:delete:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/dealerAlterRequest/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
