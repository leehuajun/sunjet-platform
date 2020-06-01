package com.sunjet.frontend.service.recycle;

import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItem;
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
 * Created by SUNJET_QRY on 2017/8/10.
 * 故障件返回通知单
 */
@Slf4j
@Service("recycleNoticeService")
public class RecycleNoticeService {

    @Autowired
    private RestClient restClient;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */

    public PageResult<RecycleNoticeItem> getPageList(PageParam<RecycleNoticeItem> pageParam) {
        try {
            log.info("RecycleNoticeServiceImpl:getPageList:success");
            return restClient.getPageList("/recycleNotice/getPageList", pageParam, new ParameterizedTypeReference<PageResult<RecycleNoticeItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */

    public RecycleNoticeInfo findOneById(String objId) {
        ResponseEntity<RecycleNoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycleNotice/findOneById", requestEntity, RecycleNoticeInfo.class);
            log.info("RecycleNoticeServiceImpl:findOneById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 新增/保存
     *
     * @param recycleNoticeInfo
     * @return
     */

    public RecycleNoticeInfo save(RecycleNoticeInfo recycleNoticeInfo) {
        try {
            HttpEntity<RecycleNoticeInfo> httpEntity = new HttpEntity<>(recycleNoticeInfo, null);
            ResponseEntity<RecycleNoticeInfo> responseEntity = restClient.post("/recycleNotice/save", httpEntity, RecycleNoticeInfo.class);
            log.info("RecycleNoticeServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 删除一个实体
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/recycleNotice/delete", httpEntity, Boolean.class);
            log.info("RecycleNoticeServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }


    /**
     * 启动流程
     *
     * @param recycleNoticeInfo
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(RecycleNoticeInfo recycleNoticeInfo, ActiveUser activeUser) {
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
        map.put("entity", recycleNoticeInfo);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/recycleNotice/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过车辆vin码查询故障件返回单
     *
     * @param vin
     * @return
     */

    public List<String> findAllRecycleNoticeObjIdsByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/recycleNotice/findAllRecycleNoticeObjIdsByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            responseEntity = restClient.delete("/recycleNotice/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
