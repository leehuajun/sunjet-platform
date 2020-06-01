package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.CommissionPartEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ActivityMaintenanceCommissionPartView;
import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceCommissionPartView;
import com.sunjet.backend.modules.asms.service.asm.CommissionPartService;
import com.sunjet.dto.asms.asm.CommissionPartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 配件需求列表
 */
@Slf4j
@RestController
@RequestMapping("/commissionPart")
public class CommissionPartController {

    @Autowired
    CommissionPartService commissionPartService;


    /**
     * 通过三包单查找配件需求列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findAllByWarrantyMaintenanceObjId")
    public List<CommissionPartInfo> findAllByWarrantyMaintenanceObjId(@RequestBody String objId) {
        return commissionPartService.findAllByWarrantyMaintenanceObjId(objId);
    }

    /**
     * 删除配件
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody String objId) {
        return commissionPartService.delete(objId);
    }


    /**
     * 通过三包单查找配件需求列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findAllByActivityMaintenanceObjId")
    public List<CommissionPartInfo> findAllByActivityMaintenanceObjId(@RequestBody String objId) {
        return commissionPartService.findAllByActivityMaintenanceObjId(objId);
    }

    /**
     * 通过三包单idList查找配件需求view
     *
     * @param warrantyMaintenanceIdList
     * @return
     */
    @PostMapping("/findAllByWarrantyMaintenanceIdList")
    public List<WarrantyMaintenanceCommissionPartView> findAllByWarrantyMaintenanceIdList(@RequestBody List<String> warrantyMaintenanceIdList) {
        return commissionPartService.findAllByWarrantyMaintenanceIdList(warrantyMaintenanceIdList);
    }

    /**
     * 通过活动单idList查找配件需求view
     *
     * @param activityMaintenanceIdList
     * @return
     */
    @PostMapping("/findAllByActivityMaintenanceIdList")
    public List<ActivityMaintenanceCommissionPartView> findAllByActivityMaintenanceIdList(@RequestBody List<String> activityMaintenanceIdList) {
        return commissionPartService.findAllByActivityMaintenanceIdList(activityMaintenanceIdList);
    }

    /**
     * 根据三包单id删除维修配件
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/deleteByWarrantyMaintenanceObjId")
    public Boolean deleteByWarrantyMaintenanceObjId(@RequestBody String objId) {
        return commissionPartService.deleteByWarrantyMaintenanceObjId(objId);
    }

    /**
     * 保存配件列表
     *
     * @param commissionPartEntityList
     * @return
     */
    @PostMapping("/saveList")
    public List<CommissionPartEntity> saveList(@RequestBody List<CommissionPartEntity> commissionPartEntityList) {
        return commissionPartService.saveList(commissionPartEntityList);
    }


}
