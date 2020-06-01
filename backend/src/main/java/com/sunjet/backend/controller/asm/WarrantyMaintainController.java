package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintainEntity;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintainService;
import com.sunjet.dto.asms.asm.WarrantyMaintainInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 维修项目
 */
@Slf4j
@RestController
@RequestMapping("/warrantyMaintain")
public class WarrantyMaintainController {

    @Autowired
    WarrantyMaintainService warrantyMaintainService;


    /**
     * 通过三包单查找维修项目列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findAllByWarrantyMaintenanceObjId")
    public List<WarrantyMaintainInfo> findAllByWarrantyMaintenanceObjId(@RequestBody String objId) {
        return warrantyMaintainService.findAllByWarrantyMaintenanceObjId(objId);
    }


    /**
     * 删除维修项目
     *
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return warrantyMaintainService.delete(objId);
    }

    /**
     * 保存维修项目列表
     *
     * @param warrantyMaintainEntityList
     * @return
     */
    @PostMapping("/saveList")
    public List<WarrantyMaintainEntity> saveList(@RequestBody List<WarrantyMaintainEntity> warrantyMaintainEntityList) {
        return warrantyMaintainService.saveList(warrantyMaintainEntityList);
    }


    /**
     * 保存维修项目
     *
     * @param warrantyMaintainEntity
     * @return
     */
    @PostMapping("/save")
    public WarrantyMaintainEntity save(@RequestBody WarrantyMaintainEntity warrantyMaintainEntity) {
        return warrantyMaintainService.save(warrantyMaintainEntity);
    }


}
