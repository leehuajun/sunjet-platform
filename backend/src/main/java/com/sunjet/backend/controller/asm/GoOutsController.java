package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import com.sunjet.backend.modules.asms.service.asm.GoOutService;
import com.sunjet.dto.asms.asm.GoOutInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 外出活动列表
 */
@Slf4j
@RestController
@RequestMapping("/goOuts")
public class GoOutsController {

    @Autowired
    GoOutService goOutService;


    /**
     * 通过三包单查找外出活动列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findAllByWarrantyMaintenanceObjId")
    public List<GoOutInfo> findAllByWarrantyMaintenanceObjId(@RequestBody String objId) {
        return goOutService.findAllByWarrantyMaintenanceObjId(objId);
    }


    /**
     * 删除外出项目
     *
     * @param objId
     * @return
     */
    @PostMapping("delete")
    public Boolean delete(@RequestBody String objId) {
        return goOutService.delete(objId);
    }


    /**
     * 通过三包单查找外出活动列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findAllByActivityMaintenanceObjId")
    public List<GoOutInfo> findAllByActivityMaintenanceObjId(@RequestBody String objId) {
        return goOutService.findAllByActivityMaintenanceObjId(objId);
    }

    /**
     * 保存外出信息列表
     *
     * @param goOutEntityList
     * @return
     */
    @PostMapping("/saveList")
    public List<GoOutEntity> saveList(@RequestBody List<GoOutEntity> goOutEntityList) {
        return goOutService.saveList(goOutEntityList);
    }


}
