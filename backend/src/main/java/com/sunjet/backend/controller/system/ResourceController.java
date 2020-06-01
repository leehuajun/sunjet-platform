package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.ResourceService;
import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.dto.system.admin.ResourceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wfb on 17-7-21.
 * 资源管理
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    /**
     * 新增
     *
     * @param resourceInfo
     * @return
     */
    @PostMapping("/save")
    public ResourceInfo save(@RequestBody ResourceInfo resourceInfo) {

        return resourceService.save(resourceInfo);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/findAll")
    public List<ResourceInfo> findAll() {
        return resourceService.findAll();
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return resourceService.delete(objId);
    }

    /**
     * @param objId
     * @return
     */
    @PostMapping("/findOneWithOperationsById")
    public ResourceInfo findOneWithOperationsById(@RequestBody String objId) {
        return resourceService.findOneWithOperationsById(objId);
    }

    /**
     * 获取所有资源 及 资源对应的操作集合
     *
     * @return
     */
    @GetMapping("/findAllWithOperations")
    public List<ResourceInfo> findAllWithOperations() {
        return resourceService.findAllWithOperations();
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ResourceItem> getPageList(@RequestBody PageParam<ResourceItem> pageParam) {
        return resourceService.getPageList(pageParam);
    }
}
