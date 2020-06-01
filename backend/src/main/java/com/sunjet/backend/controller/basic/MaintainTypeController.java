package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.service.basic.MaintainTypeService;
import com.sunjet.backend.system.service.MenuService;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by SUNJET_WS on 2017/7/20.
 * 系统
 */
@Slf4j
@RestController
@RequestMapping("/maintainType")
public class MaintainTypeController {

    @Autowired
    MaintainTypeService maintainTypeService;

    /**
     * 获取所有的菜单
     *
     * @return
     */
    @ApiOperation(value = "查询所有维修项目类别", notes = "查询所有维修项目类别")
    @PostMapping("/findAll")
    public List<MaintainTypeInfo> getMaintainTypeAll() {

        return maintainTypeService.findAll();
    }

    /**
     * 通过objId获取一个菜单
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOne")
    public MaintainTypeInfo findOneMaintainTypeById(@RequestBody String objId) {

        return maintainTypeService.findOne(objId);
    }


    /**
     * 保存实体
     *
     * @param maintainTypeInfo
     * @return
     */
    @PostMapping("/save")
    public MaintainTypeInfo saveMaintainType(@RequestBody MaintainTypeInfo maintainTypeInfo) {

        return maintainTypeService.save(maintainTypeInfo);
    }


    /**
     * 删除实体
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return maintainTypeService.deleteByObjId(objId);
    }

    /**
     * 获取所有
     *
     * @return
     */
    @ApiOperation(value = "查询所有维修项目类别", notes = "查询所有维修项目类别")
    @PostMapping("/findModels")
    public List<MaintainTypeInfo> getVehicleModels() {
        return maintainTypeService.findModels();
    }

    /**
     * 获取所有
     *
     * @return
     */
    @ApiOperation(value = "查询所有维修项目类别", notes = "查询所有维修项目类别")
    @PostMapping("/findSystems")
    public List<MaintainTypeInfo> getVehicleSystems(@RequestBody String parentId) {
        return maintainTypeService.findSystems(parentId);
    }

    /**
     * 获取所有
     *
     * @return
     */
    @ApiOperation(value = "查询所有维修项目类别", notes = "查询所有维修项目类别")
    @PostMapping("/findSubSystems")
    public List<MaintainTypeInfo> getVehicleSubSystems(@RequestBody String parentId) {
        return maintainTypeService.findSubSystems(parentId);
    }

    /**
     * 获取所有
     *
     * @return
     */
    @ApiOperation(value = "查询所有维修项目类别", notes = "查询所有维修项目类别")
    @PostMapping("/findAllByParentId")
    public List<MaintainTypeInfo> findAllByParentId(@RequestBody String parentId) {
        return maintainTypeService.findAllByParentId(parentId);
    }
}
