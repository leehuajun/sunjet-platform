package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.dto.system.admin.ResourceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface ResourceService {
    //ResourceEntity save(ResourceEntity resourceEntity);
    //
    List<ResourceInfo> findAll();

    //
    //void delete(ResourceEntity model);
    //
    ResourceInfo findOneWithOperationsById(String objId);

    List<ResourceInfo> findAllWithOperations();


    ResourceInfo findOne(String objId);

    ResourceInfo save(ResourceInfo info);

    boolean delete(String objId);

    boolean delete(ResourceInfo info);

    PageResult<ResourceItem> getPageList(PageParam<ResourceItem> pageParam);
}
