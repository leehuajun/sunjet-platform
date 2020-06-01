package com.sunjet.frontend.service.activity;

import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityDistributionItem;
import com.sunjet.dto.asms.activity.ActivityNoticeInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动分配单
 * Created by zyf on 2017/8/28.
 */
@Slf4j
@Service("activityDistributionService")
public class ActivityDistributionService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ActivityNoticeService activityNoticeService;


    public PageResult<ActivityDistributionItem> getPageList(PageParam<ActivityDistributionItem> pageParam) {
        try {
            return restClient.getPageList("/activityDistribution/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ActivityDistributionItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityDistributionServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<ActivityDistributionInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/dealerAdmitRequest/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityDistributionServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId查找活动分配单
     *
     * @param objId
     * @return
     */
    public ActivityDistributionInfo findOneById(String objId) {
        ResponseEntity<ActivityDistributionInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/activityDistribution/findOneById", requestEntity, ActivityDistributionInfo.class);
            ActivityDistributionInfo activityDistributionInfo = responseEntity.getBody();
            //获取活动分配单活动车辆
            List<ActivityVehicleItem> activityVehicleItems = restClient.findAll("/activityDistribution/findActivityVehicleItemsDistributionId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
            if (StringUtils.isNotBlank(activityDistributionInfo.getActivityNoticeId())) {
                ActivityNoticeInfo activityNoticeInfo = activityNoticeService.findOneById(activityDistributionInfo.getActivityNoticeId());
                //获取通知单
                activityDistributionInfo.setActivityNotice(activityNoticeInfo);
                //获取活动通知单配件
                activityDistributionInfo.setActivityPartItems(activityNoticeInfo.getActivityPartItems());
            }


            activityDistributionInfo.setActivityVehicleItems(activityVehicleItems);
            return activityDistributionInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityDistributionServiceImpl:findOneById:error" + e.getMessage());
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
            responseEntity = restClient.delete("/activityDistribution/deleteById", requestEntity, Boolean.class);
            log.info("ActivityDistributionServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityDistributionServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 启动流程
     *
     * @param activityDistributionRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(ActivityDistributionInfo activityDistributionRequest, ActiveUser activeUser) {
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
        map.put("entity", activityDistributionRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/activityDistribution/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 新增
     *
     * @param activityDistributionInfo
     * @return
     */

    public ActivityDistributionInfo save(ActivityDistributionInfo activityDistributionInfo) {
        ResponseEntity<ActivityDistributionInfo> responseEntity = null;
        try {
            HttpEntity<ActivityDistributionInfo> requestEntity = new HttpEntity<>(activityDistributionInfo, null);
            responseEntity = restClient.post("/activityDistribution/save", requestEntity, ActivityDistributionInfo.class);
            activityDistributionInfo = responseEntity.getBody();

            HttpEntity<String> requestEntity2 = new HttpEntity<>(activityDistributionInfo.getObjId(), null);
            //获取活动分配单活动车辆
            List<ActivityVehicleItem> activityVehicleItems = restClient.findAll("/activityDistribution/findActivityVehicleItemsDistributionId", requestEntity2, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
            if (StringUtils.isNotBlank(activityDistributionInfo.getActivityNoticeId())) {
                ActivityNoticeInfo activityNoticeInfo = activityNoticeService.findOneById(activityDistributionInfo.getActivityNoticeId());
                //获取通知单
                activityDistributionInfo.setActivityNotice(activityNoticeInfo);
                //获取活动通知单配件
                activityDistributionInfo.setActivityPartItems(activityNoticeInfo.getActivityPartItems());
            }
            activityDistributionInfo.setActivityVehicleItems(activityVehicleItems);

            return activityDistributionInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param activityDistributionInfo
     * @return
     */

    public boolean delete(ActivityDistributionInfo activityDistributionInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<ActivityDistributionInfo> requestEntity = new HttpEntity<>(activityDistributionInfo, null);
            responseEntity = restClient.delete("/activityDistribution/delete", requestEntity, Boolean.class);
            log.info("ActivityDistributionServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityDistributionServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 删除活动分配的活动车辆关系
     *
     * @param activityVehicleObjId 活动车辆id
     */
    public Boolean deleteActivityDistributionVehicleItem(String activityVehicleObjId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(activityVehicleObjId, null);
            responseEntity = restClient.delete("/activityDistribution/deleteActivityDistributionVehicleItem", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查找状态已关闭的相关服务站单据
     *
     * @param keyword    服务站搜索关键词
     * @param dealerCode 服务站编码
     * @return
     */
    public List<ActivityDistributionInfo> searchCloseActivityDistributionByDealerCodeAndKeyWord(String keyword, String dealerCode) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("keyword", keyword);
            map.put("dealerCode", dealerCode);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<ActivityDistributionInfo> activityDistributionInfoList = restClient.findAll("/activityDistribution/searchCloseActivityDistributionByDealerCodeAndKeyWord", requestEntity, new ParameterizedTypeReference<List<ActivityDistributionInfo>>() {
            });
            Set<String> activityNoticeIds = new HashSet<>();
            for (ActivityDistributionInfo activityDistributionInfo : activityDistributionInfoList) {
                activityNoticeIds.add(activityDistributionInfo.getActivityNoticeId());
            }
            List<ActivityNoticeInfo> activityNoticeInfos = activityNoticeService.findAllByObjIds(activityNoticeIds);
            Map<String, ActivityNoticeInfo> activityNoticeInfoMap = new HashMap<>();
            for (ActivityNoticeInfo activityNoticeInfo : activityNoticeInfos) {
                activityNoticeInfoMap.put(activityNoticeInfo.getObjId(), activityNoticeInfo);
            }
            //绑定活动通知单
            for (ActivityDistributionInfo activityDistributionInfo : activityDistributionInfoList) {
                ActivityNoticeInfo activityNoticeInfo = activityNoticeInfoMap.get(activityDistributionInfo.getActivityNoticeId());
                activityDistributionInfo.setActivityNotice(activityNoticeInfo);
            }


            return activityDistributionInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
