package com.sunjet.backend.controller.asm;


import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceView;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintenanceService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 三包服务单
 *
 * @author: lhj
 * @create: 2017-07-03 17:08
 * @description: 说明
 */
@Slf4j
@RestController
@RequestMapping("/warrantyMaintenance")
public class WarrantyMaintenanceController {
    @Autowired
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @Autowired
    private DocumentNoService documentNoService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<WarrantyMaintenanceView> getPageList(@RequestBody PageParam<WarrantyMaintenanceItem> pageParam) {
        return warrantyMaintenanceService.getPageList(pageParam);
    }

    /**
     * 通过objid 查找实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public WarrantyMaintenanceInfo findOneById(@RequestBody String objId) {
        return warrantyMaintenanceService.findOne(objId);
    }


    /**
     * 保存三包服务单
     *
     * @param warrantyMaintenanceInfo
     * @return
     */
    @PostMapping("/save")
    public WarrantyMaintenanceInfo save(@RequestBody WarrantyMaintenanceInfo warrantyMaintenanceInfo) {
        if (warrantyMaintenanceInfo != null && StringUtils.isBlank(warrantyMaintenanceInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(WarrantyMaintenanceEntity.class.getSimpleName());
            warrantyMaintenanceInfo.setDocNo(docNo);

        }
        return warrantyMaintenanceService.save(warrantyMaintenanceInfo);
    }


    /**
     * 通过单据类型srcDocNo查找三包单里的信息
     *
     * @param srcDocNo
     * @return
     */
    @PostMapping("/findOneWithOthersBySrcDocNo")
    public WarrantyMaintenanceInfo findOneWithOthersBySrcDocNo(@RequestBody String srcDocNo) {
        return warrantyMaintenanceService.findOneWithOthersBySrcDocNo(srcDocNo);
    }

    /**
     * 提交流程
     *
     * @param map
     * @return
     */
    @PostMapping("startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> map) {
        return warrantyMaintenanceService.startProcess(map);

    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return warrantyMaintenanceService.delete(objId);
    }

    /**
     * 通过车辆ID查所有
     *
     * @param vehicleId
     * @return
     */
    @PostMapping("/findAllByVehicleId")
    public List<WarrantyMaintenanceInfo> findAllByVehicleId(@RequestBody String vehicleId) {
        return warrantyMaintenanceService.findAllByVehicleId(vehicleId);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return warrantyMaintenanceService.desertTask(objId);
    }

}
