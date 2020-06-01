package com.sunjet.frontend.service.activity;

import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
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
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@Service("activityVehicleService")
public class ActivityVehicleService {

    @Autowired
    private RestClient restClient;


    /**
     * 获取分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<ActivityVehicleItem> getPageList(PageParam<ActivityVehicleItem> pageParam) {
        try {
            return restClient.getPageList("/activityVehicle/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ActivityVehicleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }


    public PageResult<ActivityVehicleItem> getActivityVehiclePageListScreenActivityDistributionIdIsNULL(PageParam pageParam) {
        try {
            return restClient.getPageList("/activityVehicle/getActivityVehiclePageListScreenActivityDistributionIdIsNULL", pageParam, new ParameterizedTypeReference<PageResult<ActivityVehicleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:getActivityVehiclePageListScreenActivityDistributionIdIsNULL:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 新增
     *
     * @param activityVehicleItem
     * @return
     */

    public ActivityVehicleItem save(ActivityVehicleItem activityVehicleItem) {
        ResponseEntity<ActivityVehicleItem> responseEntity = null;
        try {
            HttpEntity<ActivityVehicleItem> requestEntity = new HttpEntity<>(activityVehicleItem, null);
            responseEntity = restClient.post("/activityVehicle/save", requestEntity, ActivityVehicleItem.class);
            log.info("ActivityVehicleServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }


    public List<ActivityVehicleInfo> saveList(List<ActivityVehicleInfo> activityVehicleInfos) {
        ResponseEntity<ActivityVehicleInfo> responseEntity = null;
        try {
            HttpEntity<List<ActivityVehicleInfo>> requestEntity = new HttpEntity<>(activityVehicleInfos, null);

            List<ActivityVehicleInfo> infos = restClient.findAll("/activityVehicle/saveList", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleInfo>>() {
            });

            log.info("ActivityVehicleServiceImpl:saveList:success");
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 更新activityDistributionId
     *
     * @param activityVehicleInfoList
     * @return
     */

    public List<ActivityVehicleInfo> saveActivityDistributionId(List<ActivityVehicleInfo> activityVehicleInfoList) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityVehicleInfoList, null);
            List<ActivityVehicleInfo> activityVehicleInfos = restClient.findAll("/activityVehicle/saveActivityDistributionId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleInfo>>() {
            });
            return activityVehicleInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:saveActivityDistributionId:error:" + e.getMessage());
            return null;
        }
    }


    public Integer findCountVehicleByActivityNoticeId(String activityNoticeId) {
        ResponseEntity<Integer> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityNoticeId, null);
            responseEntity = restClient.get("/activityVehicle/findCountVehicleByActivityNoticeId", requestEntity, Integer.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:findCountVehicleByActivityNoticeId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 如果是活动通知单的时候
     * 通过objId删除
     *
     * @param objId
     */

    public boolean deleteById(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/activityVehicle/deleteById", requestEntity, Boolean.class);
            log.info("ActivityVehicleServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 如果是活动分配单的时候
     * 通过objId找到对象，删除关联
     *
     * @param objId
     */

    public boolean deleteRels(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/activityVehicle/deleteRels", requestEntity, Boolean.class);
            log.info("ActivityVehicleServiceImpl:deleteRels:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:deleteRels:error" + e.getMessage());
            return false;
        }
    }


    public List<ActivityVehicleInfo> findVehicleListByActibityNoticeId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<ActivityVehicleInfo> list = restClient.findAll("/activityVehicle/findVehicleListByActibityNoticeId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过活动分配单Id获取车辆列表
     *
     * @param objId
     * @return
     */

    public List<ActivityVehicleItem> searchActivityVehicleByActivityDistributionId(String objId, String keyword) {

        Map<String, String> map = new HashMap<>();
        map.put("objId", objId);
        map.put("keyword", keyword);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            return restClient.findAll("/activityVehicle/searchActivityVehicleByActivityDistributionId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动服务单车辆objid 查一个活动车辆
     *
     * @param activityVehicleId
     * @return
     */

    public ActivityVehicleItem findOneById(String activityVehicleId) {
        ResponseEntity<ActivityVehicleItem> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityVehicleId, null);
            responseEntity = restClient.get("/activityVehicle/findOneById", requestEntity, ActivityVehicleItem.class);
            log.info("ActivityVehicleServiceImpl:findOneById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过活动通知单ID和车辆ID删除一条活动车辆
     *
     * @param activityVehicleId
     * @return
     */

    public boolean deleteActivityVehicleById(String activityVehicleId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
//            Map<String,Object> map = new HashMap<>();
//            map.put("activityNoticeId",activityNoticeId);
//            map.put("activityVehicleId",activityVehicleId);
            HttpEntity requestEntity = new HttpEntity<>(activityVehicleId, null);
            responseEntity = restClient.get("/activityVehicle/deleteActivityVehicleById", requestEntity, Boolean.class);
            log.info("ActivityVehicleServiceImpl:deleteActivityVehicleById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:deleteActivityVehicleById:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过活动通知单ID删除该活动单下面的所有活动车辆
     *
     * @param activityNoticeId
     * @return
     */

    public boolean deleteByactivityNoticeId(String activityNoticeId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityNoticeId, null);
            responseEntity = restClient.delete("/activityVehicle/deleteByActivityNoticeId", requestEntity, Boolean.class);
            log.info("ActivityVehicleServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ActivityVehicleServiceImpl:deleteByactivityNoticeId:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过vin查询所有货单车辆objId
     *
     * @param vin
     * @return
     */

    public List<String> findAllObjIdByVin(String vin) {

        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            return restClient.findAll("/activityVehicle/findAllObjIdByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ActivityVehicleInfo> findAllByVehicleId(String vehicleId) {

        ResponseEntity<List> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(vehicleId, null);
            return restClient.findAll("/activityVehicle/findAllByVehicleId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动分配单获取活动车辆
     *
     * @param objId
     * @return
     */
    public List<ActivityVehicleItem> findAllByActivityDistributionObjid(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            //获取活动分配单活动车辆
            return restClient.findAll("/activityDistribution/findActivityVehicleItemsDistributionId", requestEntity, new ParameterizedTypeReference<List<ActivityVehicleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
