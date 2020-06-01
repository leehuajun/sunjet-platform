package com.sunjet.frontend.service.dealer;

import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestItem;
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
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@Service("dealerQuitService")
public class DealerQuitService {

    @Autowired
    private RestClient restClient;


    /**
     * 获取列表集合
     *
     * @return
     */

    public List<DealerQuitRequestInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerQuitRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerQuitRequestItem> getPageList(PageParam<DealerQuitRequestItem> pageParam) {
        try {
            return restClient.getPageList("/dealerQuitRequest/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerQuitRequestItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public DealerQuitRequestInfo findOneById(String objId) {
        ResponseEntity<DealerQuitRequestInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/dealerQuitRequest/findOneById", requestEntity, DealerQuitRequestInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }


    public Map<String, String> startProcess(DealerQuitRequestInfo dealerQuitRequest, ActiveUser activeUser) {
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
        map.put("entity", dealerQuitRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/dealerQuitRequest/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/dealerQuitRequest/deleteByObjId", requestEntity, Boolean.class);
            log.info("DealerQuitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param dealerQuitRequestInfo
     * @return
     */

    public DealerQuitRequestInfo save(DealerQuitRequestInfo dealerQuitRequestInfo) {
        ResponseEntity<DealerQuitRequestInfo> responseEntity = null;
        try {
            HttpEntity<DealerQuitRequestInfo> requestEntity = new HttpEntity<>(dealerQuitRequestInfo, null);
            responseEntity = restClient.post("/dealerQuitRequest/save", requestEntity, DealerQuitRequestInfo.class);
            log.info("DealerQuitServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param dealerQuitRequestInfo
     * @return
     */

    public boolean delete(DealerQuitRequestInfo dealerQuitRequestInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<DealerQuitRequestInfo> requestEntity = new HttpEntity<>(dealerQuitRequestInfo, null);
            responseEntity = restClient.delete("/dealerQuitRequest/delete", requestEntity, Boolean.class);
            log.info("DealerQuitServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerQuitServiceImpl:delete:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/dealerQuitRequest/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
