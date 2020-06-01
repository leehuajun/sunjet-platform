package com.sunjet.frontend.service.activity;

import com.sunjet.dto.asms.activity.ActivityPartInfo;
import com.sunjet.dto.asms.activity.ActivityPartItem;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动配件
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@Service("activityPartService")
public class ActivityPartService {

    @Autowired
    private RestClient restClient;


    public List<ActivityPartItem> findPartByActivityNoticeId(String activityNoticeId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityNoticeId, null);
            List<ActivityPartItem> list = restClient.findAll("/activityPart/findPartByActivityNoticeId", requestEntity, new ParameterizedTypeReference<List<ActivityPartItem>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityPartServiceImpl:findPartByActivityNoticeId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public ActivityPartInfo findOneById(String objId) {
        ResponseEntity<ActivityPartInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/activityPart/findOneById", requestEntity, ActivityPartInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityPartServiceImpl:findOneById:error" + e.getMessage());
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
            responseEntity = restClient.delete("/activityPart/deleteById", requestEntity, Boolean.class);
            log.info("ActivityPartServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityPartServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 新增
     *
     * @param activityPartInfo
     * @return
     */

    public ActivityPartInfo save(ActivityPartInfo activityPartInfo) {
        ResponseEntity<ActivityPartInfo> responseEntity = null;
        try {
            HttpEntity<ActivityPartInfo> requestEntity = new HttpEntity<>(activityPartInfo, null);
            responseEntity = restClient.post("/activityPart/save", requestEntity, ActivityPartInfo.class);
            log.info("ActivityPartServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityPartServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过活动通知单ID删除该活动单下面的所有活动配件
     *
     * @param activityNoticeId
     * @return
     */

    public boolean deleteByactivityNoticeId(String activityNoticeId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityNoticeId, null);
            responseEntity = restClient.delete("/activityPart/deleteByActivityNoticeId", requestEntity, Boolean.class);
            log.info("ActivityPartServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityPartServiceImpl:deleteByactivityNoticeId:error" + e.getMessage());
            return false;
        }
    }

}
