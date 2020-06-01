package com.sunjet.backend.controller.activity;

import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.service.activity.ActivityVehicleService;
import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@RestController
@RequestMapping("/activityVehicle")
public class ActivityVehicleController {

    @Autowired
    private ActivityVehicleService activityVehicleService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ActivityVehicleView> getPageList(@RequestBody PageParam<ActivityVehicleItem> pageParam) {
        return activityVehicleService.getPageList(pageParam);
    }

    /**
     * 分页查询,查活动通知单对应活动车辆，并且筛选出AadId（分配单ID）为空的车辆
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getActivityVehiclePageListScreenActivityDistributionIdIsNULL")
    public PageResult<ActivityVehicleView> getactivityVehiclePageListScreenActivityDistributionIdIsNULL(@RequestBody PageParam<ActivityVehicleItem> pageParam) {
        return activityVehicleService.getactivityVehiclePageListScreenActivityDistributionIdIsNULL(pageParam);
    }

    @PostMapping("/save")
    public ActivityVehicleEntity save(@RequestBody ActivityVehicleEntity activityVehicleEntity) {
        return activityVehicleService.save(activityVehicleEntity);
    }

    @PostMapping("/saveList")
    public List<ActivityVehicleEntity> save(@RequestBody List<ActivityVehicleEntity> list) {
        return activityVehicleService.saveList(list);
    }

    @PostMapping("/saveActivityDistributionId")
    public List<ActivityVehicleInfo> saveActivityDistributionId(@RequestBody List<ActivityVehicleInfo> activityVehicleInfoList) {
        return activityVehicleService.saveActivityDistributionId(activityVehicleInfoList);
    }

    @PostMapping("/findCountVehicleByActivityNoticeId")
    public Integer findCountVehicleByActivityNoticeId(@RequestBody String activityNoticeId) {
        return activityVehicleService.findCountVehicleByActivityNoticeId(activityNoticeId);
    }

    @DeleteMapping("/deleteById")
    public boolean deleteByObjId(@RequestBody String objId) {
        return activityVehicleService.deleteById(objId);
    }

    @PostMapping("/deleteRels")
    public boolean deleteRels(@RequestBody String objId) {
        return activityVehicleService.deleteRels(objId);
    }

    @PostMapping("/findVehicleListByActibityNoticeId")
    public List<ActivityVehicleInfo> findVehicleListByActibityNoticeId(@RequestBody String objId) {
        return activityVehicleService.findVehicleListByActivityNoticeId(objId);
    }


    /**
     * 通过活动分配单objid获取活动单Id
     *
     * @param map
     * @return
     */
    @PostMapping("searchActivityVehicleByActivityDistributionId")
    public List<ActivityVehicleView> searchActivityVehicleByActivityDistributionId(@RequestBody Map<String, String> map) {
        return activityVehicleService.searchActivityVehicleByActivityDistributionId(map.get("objId"), map.get("keyword"));
    }

    /**
     * 通过objid查询一个活动车辆视图
     *
     * @param objId
     * @return
     */
    @PostMapping("findOneById")
    public ActivityVehicleView findOneById(@RequestBody String objId) {
        return activityVehicleService.findOneById(objId);
    }

    /**
     * 刪除活動车辆
     *
     * @param activityVehicleId
     * @return
     */
    @PostMapping("/deleteActivityVehicleById")
    public boolean deleteActivityVehicleById(@RequestBody String activityVehicleId) {
        return activityVehicleService.deleteActivityVehicleById(activityVehicleId);
    }

    /**
     * 通过活动通知单ID删除该活动单下面的所有活动车辆
     *
     * @param activityNoticeId
     * @return
     */
    @DeleteMapping("/deleteByActivityNoticeId")
    public boolean deleteByActivityNoticeId(@RequestBody String activityNoticeId) {
        return activityVehicleService.deleteByActivityNoticeId(activityNoticeId);
    }

    /**
     * 通过Vin查询活动车辆objid
     *
     * @param map
     * @return
     */
    @PostMapping("findAllObjIdByVin")
    public List<String> findAllObjIdByVin(@RequestBody Map<String, String> map) {
        return activityVehicleService.findAllObjIdByVin(map.get("vin"));
    }

    /**
     * 通过车辆ID查所有
     *
     * @param vehicleId
     * @return
     */
    @PostMapping("/findAllByVehicleId")
    public List<ActivityVehicleEntity> findAllByVehicleId(@RequestBody String vehicleId) {
        return activityVehicleService.findAllByVehicleId(vehicleId);

    }

}
