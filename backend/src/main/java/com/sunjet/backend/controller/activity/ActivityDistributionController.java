package com.sunjet.backend.controller.activity;

import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityDistributionView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.service.activity.ActivityDistributionService;
import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityDistributionItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动分配
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@RestController
@RequestMapping("/activityDistribution")
public class ActivityDistributionController {

    @Autowired
    private ActivityDistributionService activityDistributionService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ActivityDistributionView> getPageList(@RequestBody PageParam<ActivityDistributionItem> pageParam) {
        return activityDistributionService.getPageList(pageParam);
    }

    @GetMapping("/findAll")
    public List<ActivityDistributionInfo> findAll() {
        return activityDistributionService.findAll();
    }

    @PostMapping("/findOneById")
    public ActivityDistributionEntity findOneById(@RequestBody String objId) {
        return activityDistributionService.findOneById(objId);
    }

    //保存活动分配单
    @PostMapping("/save")
    public ActivityDistributionEntity save(@RequestBody ActivityDistributionEntity activityDistributionEntity) {
        return activityDistributionService.save(activityDistributionEntity);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody ActivityDistributionInfo info) {
        return activityDistributionService.delete(info);
    }

    @DeleteMapping("/deleteById")
    public boolean deleteById(@RequestBody String objId) {
        return activityDistributionService.deleteById(objId);
    }


    /**
     * 查找状态已关闭的相关服务站单据
     *
     * @param map 搜索条件
     * @return
     */
    @PostMapping("searchCloseActivityDistributionByDealerCodeAndKeyWord")
    public List<ActivityDistributionEntity> searchCloseActivityDistributionByDealerCodeAndKeyWord(@RequestBody Map<String, String> map) {
        return activityDistributionService.findAllByStatusAndKeywordAndDealerCode(map.get("keyword"), map.get("dealerCode"));
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return activityDistributionService.startProcess(variables);
    }

    /**
     * 通过活动分配单ID查找对应的活动车辆
     *
     * @param distributionId
     * @return
     */
    @PostMapping("/findActivityVehicleItemsDistributionId")
    public List<ActivityVehicleView> findActivityVehicleItemsDistributionId(@RequestBody String distributionId) {
        return activityDistributionService.findActivityVehicleItemsDistributionId(distributionId);
    }

    /**
     * 解除活动车辆ID
     *
     * @param activityVehicleObjId
     * @return
     */
    @DeleteMapping("deleteActivityDistributionVehicleItem")
    public Boolean deleteActivityDistributionVehicleItem(@RequestBody String activityVehicleObjId) {
        return activityDistributionService.deleteActivityDistributionVehicleItem(activityVehicleObjId);
    }


}
