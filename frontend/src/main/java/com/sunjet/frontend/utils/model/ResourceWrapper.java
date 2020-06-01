package com.sunjet.frontend.utils.model;

import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.ResourceInfo;

import java.util.List;

/**
 * @author lhj
 * @create 2016-02-25 下午5:26
 */
public class ResourceWrapper {
    private ResourceInfo entity;
    private List<EntityWrapper<PermissionInfo>> entityWrappers;

    public ResourceInfo getEntity() {
        return entity;
    }

    public void setEntity(ResourceInfo model) {
        this.entity = model;
    }

    public List<EntityWrapper<PermissionInfo>> getEntityWrappers() {
        return entityWrappers;
    }

    public void setEntityWrappers(List<EntityWrapper<PermissionInfo>> entityWrappers) {
        this.entityWrappers = entityWrappers;
    }

    public ResourceWrapper() {
    }

    public ResourceWrapper(ResourceInfo entity, List<EntityWrapper<PermissionInfo>> entityWrappers) {
        this.entity = entity;
        this.entityWrappers = entityWrappers;
    }
}
