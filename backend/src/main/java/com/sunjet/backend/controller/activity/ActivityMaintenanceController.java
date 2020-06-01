package com.sunjet.backend.controller.activity;

import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityMaintenanceView;
import com.sunjet.backend.modules.asms.service.activity.ActivityMaintenanceService;
import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/activityMaintenance")
public class ActivityMaintenanceController {

    @Autowired
    private ActivityMaintenanceService activityMaintenanceService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ActivityMaintenanceView> getPageList(@RequestBody PageParam<ActivityMaintenanceItem> pageParam) {
        return activityMaintenanceService.getPageList(pageParam);
    }


    /**
     * 删除
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody String objId) {
        return activityMaintenanceService.delete(objId);
    }

    /**
     * 通过objId 查找活动服务实体
     *
     * @param objId
     * @return
     */
    @PostMapping("findOneById")
    public ActivityMaintenanceEntity findOneById(@RequestBody String objId) {
        return activityMaintenanceService.findOneById(objId);
    }

    /**
     * 保存实体
     *
     * @param activityMaintenanceInfo
     * @return
     */
    @PostMapping("save")
    public ActivityMaintenanceInfo save(@RequestBody ActivityMaintenanceInfo activityMaintenanceInfo) {
        return activityMaintenanceService.save(activityMaintenanceInfo);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return activityMaintenanceService.startProcess(variables);
    }

    /**
     * 通过车辆ID查所有
     *
     * @param vehicleIds
     * @return
     */
    @PostMapping("/findAllByVehicleIds")
    public List<ActivityMaintenanceInfo> findAllByVehicleIds(@RequestBody List<String> vehicleIds) {
        return activityMaintenanceService.findAllByVehicleIds(vehicleIds);

    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return activityMaintenanceService.desertTask(objId);
    }
}
