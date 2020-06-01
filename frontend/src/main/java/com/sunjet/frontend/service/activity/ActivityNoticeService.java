package com.sunjet.frontend.service.activity;

import com.sunjet.dto.asms.activity.ActivityNoticeInfo;
import com.sunjet.dto.asms.activity.ActivityNoticeItem;
import com.sunjet.dto.asms.activity.ActivityPartItem;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
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
import java.util.Set;

/**
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@Service("activityNoticeService")
public class ActivityNoticeService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<ActivityNoticeInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerAdmitRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<ActivityNoticeItem> getPageList(PageParam<ActivityNoticeItem> pageParam) {
        try {
            return restClient.getPageList("/activityNotice/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ActivityNoticeItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public ActivityNoticeInfo findOneById(String objId) {
        ResponseEntity<ActivityNoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/activityNotice/findOneById", requestEntity, ActivityNoticeInfo.class);
            ActivityNoticeInfo activityNoticeInfo = responseEntity.getBody();

            List<ActivityVehicleItem> activityVehicleItems = restClient.findAll("/activityNotice/findActivityVehicleItemsByNoticeId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
            List<ActivityPartItem> activityPartItems = restClient.findAll("/activityNotice/findActivityPartItemsByNoticeId", requestEntity, new ParameterizedTypeReference<List<ActivityPartItem>>() {
            });

            activityNoticeInfo.setActivityVehicleItems(activityVehicleItems);
            activityNoticeInfo.setActivityPartItems(activityPartItems);

            return activityNoticeInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:findOneById:error" + e.getMessage());
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
            responseEntity = restClient.delete("/activityNotice/deleteById", requestEntity, Boolean.class);
            log.info("ActivityNoticeServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增/保存
     *
     * @param activityNoticeInfo
     * @return
     */

    public ActivityNoticeInfo save(ActivityNoticeInfo activityNoticeInfo) {
        ResponseEntity<ActivityNoticeInfo> responseEntity = null;
        try {
            HttpEntity<ActivityNoticeInfo> requestEntity = new HttpEntity<>(activityNoticeInfo, null);
            responseEntity = restClient.post("/activityNotice/save", requestEntity, ActivityNoticeInfo.class);

            activityNoticeInfo = responseEntity.getBody();

            HttpEntity requestEntity2 = new HttpEntity<>(activityNoticeInfo.getObjId(), null);
            List<ActivityVehicleItem> activityVehicleItems = restClient.findAll("/activityNotice/findActivityVehicleItemsByNoticeId", requestEntity2, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
            List<ActivityPartItem> activityPartItems = restClient.findAll("/activityNotice/findActivityPartItemsByNoticeId", requestEntity2, new ParameterizedTypeReference<List<ActivityPartItem>>() {
            });

            activityNoticeInfo.setActivityVehicleItems(activityVehicleItems);
            activityNoticeInfo.setActivityPartItems(activityPartItems);

            log.info("ActivityNoticeServiceImpl:save:success");
            return activityNoticeInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param activityNoticeInfo
     * @return
     */

    public boolean delete(ActivityNoticeInfo activityNoticeInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<ActivityNoticeInfo> requestEntity = new HttpEntity<>(activityNoticeInfo, null);
            responseEntity = restClient.delete("/activityNotice/delete", requestEntity, Boolean.class);
            log.info("ActivityNoticeServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityNoticeServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 搜素已关闭的活动通知单
     *
     * @param keyWord
     * @return
     */
    public List<ActivityNoticeInfo> searchCloseActivityNotices(String keyWord) {
        ResponseEntity<List> responseEntity = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("keyWord", keyWord);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            return restClient.findAll("/activityNotice/searchCloseActivityNotices", requestEntity, new ParameterizedTypeReference<List<ActivityNoticeInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 启动流程
     *
     * @param activityNoticeRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(ActivityNoticeInfo activityNoticeRequest, ActiveUser activeUser) {
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
        map.put("entity", activityNoticeRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/activityNotice/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过供货单通知单单号查询调拨通知单子行
     *
     * @param docNo
     * @return
     */

    public List<SupplyItemInfo> findSupplyItemIdsByDocNo(String docNo) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(docNo, null);
            List<SupplyItemInfo> list = restClient.findAll("/activityNotice/findSupplyItemIdsByDocNo", requestEntity, new ParameterizedTypeReference<List<SupplyItemInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param activityNoticeDocNo
     * @return
     */

    public List<String> findAllobjIdByDocNo(String activityNoticeDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("activityNoticeDocNo", activityNoticeDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/activityNotice/findAllobjIdByDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ActivityNoticeInfo> findAllByObjIds(Set<String> objIds) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objIds, null);
            List<ActivityNoticeInfo> list = restClient.findAll("/activityNotice/findAllByObjIds", requestEntity, new ParameterizedTypeReference<List<ActivityNoticeInfo>>() {
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
            responseEntity = restClient.delete("/activityNotice/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
