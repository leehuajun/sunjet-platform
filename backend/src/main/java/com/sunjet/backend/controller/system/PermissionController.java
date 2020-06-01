package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.PermissionService;
import com.sunjet.dto.system.admin.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wfb on 17-7-25.
 * 权限管理
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    /**
     * 通过资源编号 删除对应打权限
     *
     * @param resourceCode
     * @return
     */
    @DeleteMapping("/deleteAllByResourceCode")
    public boolean deleteAllByResourceCode(@RequestBody String resourceCode) {
        return permissionService.deleteAllByResourceCode(resourceCode);
    }

    /**
     * 新增
     *
     * @param permissionInfo
     * @return
     */
    @PostMapping("/save")
    public PermissionInfo save(@RequestBody PermissionInfo permissionInfo) {

        return permissionService.save(permissionInfo);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/findAll")
    public List<PermissionInfo> findAll() {
        return permissionService.findAll();
    }


}
