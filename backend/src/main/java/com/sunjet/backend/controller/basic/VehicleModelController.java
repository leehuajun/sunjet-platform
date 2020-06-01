package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.service.basic.VehicleModelService;
import com.sunjet.backend.modules.asms.service.basic.VehicleService;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfoExt;
import com.sunjet.dto.asms.basic.VehicleModelInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/27.
 * 车辆信息
 */
@Slf4j
@RestController
@RequestMapping("/vehicleModel")
public class VehicleModelController {
    @Autowired
    private VehicleModelService vehicleModelService;

    @PostMapping("/findOne")
    public VehicleModelInfo findOne(@RequestBody String objId) {
        return vehicleModelService.findOne(objId);
    }

    @PostMapping("/findAll")
    public List<VehicleModelInfo> findAll() {
        return vehicleModelService.findAll();
    }

    @PostMapping("/save")
    public VehicleModelInfo save(@RequestBody VehicleModelInfo info) {
        return vehicleModelService.save(info);
    }

    @PostMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return vehicleModelService.delete(objId);
    }

}
