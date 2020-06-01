package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.view.FirstMaintenanceView;
import com.sunjet.backend.modules.asms.service.asm.FirstMaintenanceService;
import com.sunjet.backend.modules.asms.service.asm.GoOutService;
import com.sunjet.backend.modules.asms.service.basic.VehicleService;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceItem;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wfb on 17-8-1.
 */
@Slf4j
@RestController
@RequestMapping("/firstMaintenance")
public class FirstMaintenanceController {

    @Autowired
    private FirstMaintenanceService firstMaintenanceService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private GoOutService goOutService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<FirstMaintenanceView> getPageList(@RequestBody PageParam<FirstMaintenanceItem> pageParam) {
        PageResult<FirstMaintenanceView> pageResult = firstMaintenanceService.getPageList(pageParam);
        return pageResult;
    }

    /**
     * 获取首保服务单信息
     *
     * @param businessId
     * @return
     */
    @PostMapping("/findOneWithGoOutsById")
    public FirstMaintenanceInfo findOneWithGoOutsById(@RequestBody String businessId) {
        FirstMaintenanceInfo info = firstMaintenanceService.findOneWithGoOutsById(businessId);

        //获取对应的车辆信息
        if (StringUtils.isNotBlank(info.getVehicleId())) {
            info.setVehicleInfo(vehicleService.findOne(info.getVehicleId()));
        }

        //获取对应的外出信息
        List<GoOutInfo> goOutInfoList = goOutService.findAllByFirstMaintenanceId(info.getObjId());
        info.setGoOuts(goOutInfoList);

        return info;
    }


    /**
     * 保存
     *
     * @param firstMaintenanceInfo
     * @return
     */
    @PostMapping("/save")
    public FirstMaintenanceInfo save(@RequestBody FirstMaintenanceInfo firstMaintenanceInfo) {
        return firstMaintenanceService.save(firstMaintenanceInfo);
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody String objId) {
        return firstMaintenanceService.delete(objId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return firstMaintenanceService.startProcess(variables);
    }

    @PostMapping("/findOneByVehicleId")
    public FirstMaintenanceInfo findOneByVehicleId(@RequestBody String vehicleId) {
        return firstMaintenanceService.findOneByVehicleId(vehicleId);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return firstMaintenanceService.desertTask(objId);
    }

}
