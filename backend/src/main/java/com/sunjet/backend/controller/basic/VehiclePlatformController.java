package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.service.basic.NoticeService;
import com.sunjet.backend.modules.asms.service.basic.VehiclePlatformService;
import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 公告
 */
@Slf4j
@RestController
@RequestMapping("/vehiclePlatform")
public class VehiclePlatformController {

    @Autowired
    VehiclePlatformService vehiclePlatformService;

    @ApiOperation(value = "保存一个对象")
    @PostMapping("/save")
    public VehiclePlatformInfo save(@RequestBody VehiclePlatformInfo info) {
        return vehiclePlatformService.save(info);
    }

    @ApiOperation(value = "删除一个对象")
    @PostMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return vehiclePlatformService.delete(objId);
    }

    @ApiOperation(value = "查找一个对象", notes = "通过objId获取一个对象Info")
    @ApiImplicitParam(name = "objId", value = "实体objId", required = true, dataType = "String")
    @PostMapping("/findOne")
    public VehiclePlatformInfo findOneById(@RequestBody String objId) {
        return vehiclePlatformService.findOne(objId);
    }

    @ApiOperation(value = "查找所有车型平台")
    @PostMapping("/findAll")
    public List<VehiclePlatformInfo> findAll() {
        return vehiclePlatformService.findAll();
    }
}
