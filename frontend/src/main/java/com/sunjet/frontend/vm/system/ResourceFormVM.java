package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.frontend.service.system.OperationService;
import com.sunjet.frontend.service.system.PermissionService;
import com.sunjet.frontend.service.system.ResourceService;
import com.sunjet.frontend.utils.model.EntityWrapper;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.utils.common.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 * 资源管理
 */
public class ResourceFormVM extends FormVM {

    @WireVariable
    private ResourceService resourceService;
    @WireVariable
    private OperationService operationService;
    @WireVariable
    private PermissionService permissionService;

    @Getter
    @Setter
    private ResourceInfo resourceInfo = new ResourceInfo();
    @Getter
    @Setter
    private List<EntityWrapper<OperationInfo>> entityWrappers = new ArrayList<>();
    @Getter
    @Setter
    private List<EntityWrapper<OperationInfo>> entityWrapperSelectedItems = new ArrayList<>();

    private List<OperationInfo> operationInfoList = new ArrayList<>();


    @Init(superclass = true)
    public void init() {

        operationInfoList = operationService.findAll();

        //Collections.sort(operationInfoList);

        if (StringUtils.isNotBlank(objId)) {
            toUpdate();//新增
        } else {
            toAdd();//修改
        }
    }

    /**
     * to 新增信息
     */
    private void toAdd() {
        try {
            for (Object o : this.operationInfoList) {

                OperationInfo operationInfo = JsonHelper.map2Bean(o, OperationInfo.class);

                entityWrappers.add(new EntityWrapper(operationInfo, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * to 修改信息
     */
    private void toUpdate() {
        try {
            ResourceInfo resource = resourceService.findOneWithOperationsById(objId);
            ResourceInfo rm = resourceService.findOneWithOperationsById(objId);

            if (resource != null && rm != null) {
                resource.setOperationInfoList(new ArrayList<>());
                resource.setOperationInfoList(rm.getOperationInfoList());
            }

            resourceInfo = resource;

            for (Object o : this.operationInfoList) {

                OperationInfo operationInfo = JsonHelper.map2Bean(o, OperationInfo.class);

                Boolean found = false;
                if (resourceInfo != null && resourceInfo.getOperationInfoList() != null && resourceInfo.getOperationInfoList().size() > 0) {
                    for (OperationInfo entity : resourceInfo.getOperationInfoList()) {
                        if (entity.getObjId().equals(operationInfo.getObjId())) {
                            found = true;
                        }
                    }
                }
                if (found) {
                    EntityWrapper<OperationInfo> modelWrapper = new EntityWrapper<>(operationInfo, true);
                    entityWrappers.add(modelWrapper);
                    entityWrapperSelectedItems.add(modelWrapper);
                } else {
                    entityWrappers.add(new EntityWrapper(operationInfo, false));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存信息
     */
    @Command
//    @NotifyChange("resourceInfo")
    public void submit() {

        if (resourceInfo != null && resourceInfo.getOperationInfoList() != null) {
            this.resourceInfo.getOperationInfoList().clear();
        } else {
            this.resourceInfo.setOperationInfoList(new ArrayList<>());
        }
        for (EntityWrapper<OperationInfo> modelWrapper : entityWrapperSelectedItems) {
            this.resourceInfo.getOperationInfoList().add(modelWrapper.getEntity());
        }

        //删除资源对应的权限
        permissionService.deleteAllByResourceCode(resourceInfo.getCode());

        //重新生成权限
        for (OperationInfo operationInfo : resourceInfo.getOperationInfoList()) {
            PermissionInfo permissionInfo = new PermissionInfo();
            permissionInfo.setAccessName(operationInfo.getOptName());
            permissionInfo.setResourceName(resourceInfo.getName());
            permissionInfo.setPermissionCode(resourceInfo.getCode() + ":" + operationInfo.getOptCode());
            permissionInfo.setSeq(operationInfo.getSeq());
            permissionService.save(permissionInfo);
        }

        //保存实体并绑定关联关系
        resourceInfo = resourceService.save(resourceInfo);
        showDialog();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RESOURCE_LIST, null);
    }

}
