package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.frontend.service.system.PermissionService;
import com.sunjet.frontend.service.system.ResourceService;
import com.sunjet.frontend.service.system.RoleService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.model.EntityWrapper;
import com.sunjet.frontend.utils.model.ResourceWrapper;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.utils.common.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;

import java.util.*;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class RoleFormVM extends FormVM {

    // 这个用于测试当前选中的checkbox
    @Wire(".pgrid")
    private Grid pgrid;

    @WireVariable
    private RoleService roleService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PermissionService permissionService;
    @WireVariable
    private ResourceService resourceService;

    @Getter
    @Setter
    private Map<String, PermissionInfo> permissionMap = new HashMap<String, PermissionInfo>();
    @Getter
    @Setter
    private List<EntityWrapper<UserInfo>> userWrappers = new ArrayList<>();
    @Getter
    @Setter
    private List<EntityWrapper<UserInfo>> userWrapperSelectedItems = new ArrayList<>();
    @Getter
    @Setter
    private List<EntityWrapper<PermissionInfo>> permissionWrapperSelectedItems = new ArrayList<>();
    @Getter
    @Setter
    private List<ResourceWrapper> resourceWrappers = new ArrayList<>();
    private List<EntityWrapper<ResourceWrapper>> resourceWrapperSelectedItems = new ArrayList<>();
    @Getter
    @Setter
    private RoleInfo roleInfo = new RoleInfo();
    @Getter
    @Setter
    private List<ResourceInfo> resourcesWithOperations = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            roleInfo = roleService.findOneWithUsersAndPermissionsById(objId);
        } else {
            roleInfo = new RoleInfo();
        }
        permissionMap.clear();

        InitUserWrapperList();
        InitResourceWrapperList();
    }

    private void InitUserWrapperList() {
        try {
            userWrappers.clear();
            userWrapperSelectedItems.clear();
            List<UserInfo> users = userService.findAll();
            if (roleInfo != null) {
                for (UserInfo userEntity : users) {

                    Boolean found = false;
                    if (roleInfo.getUserInfos() != null) {
                        for (UserInfo model : roleInfo.getUserInfos()) {
                            if (model.getObjId().equals(userEntity.getObjId())) {
                                found = true;
                            }
                        }
                    }
                    if (found) {
                        EntityWrapper<UserInfo> userWrapper = new EntityWrapper<UserInfo>(userEntity, true);
                        userWrappers.add(userWrapper);
                        userWrapperSelectedItems.add(userWrapper);
                    } else {
                        userWrappers.add(new EntityWrapper<UserInfo>(userEntity, false));
                    }
                }
            } else {
                for (UserInfo userEntity : users) {
                    userWrappers.add(new EntityWrapper<UserInfo>(userEntity, false));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitResourceWrapperList() {

        try {
            resourceWrappers.clear();

            List<PermissionInfo> permissionEntities = permissionService.findAll();

            for (PermissionInfo model : permissionEntities) {
                permissionMap.put(model.getPermissionCode(), model);
            }

            List<ResourceInfo> resourceEntities = resourceService.findAll();
            List<ResourceInfo> resourcesWithOperations = resourceService.findAllWithOperations();

            if (roleInfo != null) {
                for (Object o : resourceEntities) {

                    ResourceInfo resourceInfo = JsonHelper.map2Bean(o, ResourceInfo.class);

                    ResourceWrapper resourceWrapper = new ResourceWrapper();

                    List<EntityWrapper<PermissionInfo>> permissionWrappers = new ArrayList<>();

                    resourceWrapper.setEntity(resourceInfo);

                    for (PermissionInfo permissionEntity : permissionEntities) {

                        if (permissionEntity.getPermissionCode().split(":")[0].equalsIgnoreCase(resourceInfo.getCode())) {
                            Boolean found = false;
                            if (roleInfo.getPermissions() != null) {
                                for (PermissionInfo model : roleInfo.getPermissions()) {
                                    if (model.getPermissionCode().equals(permissionEntity.getPermissionCode())) {
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            if (found) {
                                permissionWrappers.add(new EntityWrapper<>(permissionEntity, true));
                            } else {
                                permissionWrappers.add(new EntityWrapper<>(permissionEntity, false));
                            }
                        }
                    }
                    //排序
                    Collections.sort(permissionWrappers, new Comparator<EntityWrapper<PermissionInfo>>() {
                        @Override
                        public int compare(EntityWrapper<PermissionInfo> permissionWrapper01, EntityWrapper<PermissionInfo> permissionWrapper02) {
                            //比较每个ArrayList的第二个元素
                            if (permissionWrapper01.getEntity().getSeq().equals(permissionWrapper02.getEntity().getSeq())) {
                                return 0;
                            } else if (permissionWrapper01.getEntity().getSeq() < permissionWrapper02.getEntity().getSeq()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });

                    Collections.reverse(permissionWrappers);
                    resourceWrapper.setEntityWrappers(permissionWrappers);
                    resourceWrappers.add(resourceWrapper);
                }
            } else {
                for (ResourceInfo resourceInfo : resourceEntities) {
                    ResourceWrapper resourceWrapper = new ResourceWrapper();

                    List<EntityWrapper<PermissionInfo>> modelWrappers = new ArrayList<>();

                    resourceWrapper.setEntity(resourceInfo);
                    for (PermissionInfo permissionEntity : permissionEntities) {
                        if (permissionEntity.getPermissionCode().contains(resourceInfo.getCode() + ":")) {
                            //EntityWrapper<PermissionInfo> modelWrapper = new EntityWrapper<>(permissionEntity,false);
                            modelWrappers.add(new EntityWrapper<>(permissionEntity, false));
                            //resourceWrapper.setEntityWrapper(modelWrapper);
                        }
                    }

                    //排序
                    Collections.sort(modelWrappers, new Comparator<EntityWrapper<PermissionInfo>>() {
                        @Override
                        public int compare(EntityWrapper<PermissionInfo> permissionWrapper01, EntityWrapper<PermissionInfo> permissionWrapper02) {
                            //比较每个ArrayList的第二个元素
                            if (permissionWrapper01.getEntity().getSeq().equals(permissionWrapper02.getEntity().getSeq())) {
                                return 0;
                            } else if (permissionWrapper01.getEntity().getSeq() < permissionWrapper02.getEntity().getSeq()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });

                    Collections.reverse(modelWrappers);

                    resourceWrapper.setEntityWrappers(modelWrappers);
                    resourceWrappers.add(resourceWrapper);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
    }

//    private void initCheckbox() {
//
//        Integer col_size = pgrid.getColumns().getChildren().size();
//        Integer row_size = pgrid.getRows().getChildren().size();
//
////        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
//        List<Checkbox> chks = new ArrayList<>();
//        for (int i = 0; i < row_size; i++) {
//            int count = pgrid.getCell(i, 1).getChildren().size();
//            for (int j = 0; j < count; j++) {
//                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
//
//            }
//        }
//    }

    @Command
    public void test() {
        Integer col_size = pgrid.getColumns().getChildren().size();
        Integer row_size = pgrid.getRows().getChildren().size();

//        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
        List<Checkbox> chks = new ArrayList<>();
        for (int i = 0; i < row_size; i++) {
            int count = pgrid.getCell(i, 1).getChildren().size();
            for (int j = 0; j < count; j++) {
                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
                if (checkbox.isChecked())
                    chks.add(checkbox);

            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chks.size(); i++) {
            if (i == chks.size() - 1)
                sb.append(chks.get(i).getValue().toString());
            else
                sb.append(chks.get(i).getValue() + " | ");
        }
        ZkUtils.showInformation("选中的checkbox数量:" + chks.size() + "\r\n" + sb.toString(), "测试");
    }

    @Command
    @NotifyChange("roleInfo")
    public void submit() {
        try {
            if (roleInfo.getUserInfos() != null) {
                roleInfo.getUserInfos().clear();
            } else {
                roleInfo.setUserInfos(new ArrayList<>());
            }
            if (roleInfo.getPermissions() != null) {
                roleInfo.getPermissions().clear();
            } else {
                roleInfo.setPermissions(new ArrayList<>());
            }

            //绑定用户
            for (EntityWrapper<UserInfo> userWrapper : userWrapperSelectedItems) {
                roleInfo.getUserInfos().add(userWrapper.getEntity());
            }

            List<String> chkValues = getCheckboxValue();

            //绑定权限
            for (String value : chkValues) {
                roleInfo.getPermissions().add(permissionMap.get(value));
            }

            //1.保存角色信息
            roleInfo = roleService.save(roleInfo);

            ////2.删除角色所绑定的用户
            //roleService.removeUsersFromRole(roleInfo.getObjId());
            //
            ////3.重新绑定用户
            //roleService.addUsersToRole(roleInfo);
            //
            ////4.删除角色 与 权限 的关联关系
            //roleService.deleteUserWithPermission(roleInfo.getObjId());
            //
            ////5.保存角色 与 权限 的关联关系
            //roleService.addUsersWithPermission(roleInfo);
            showDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ROLE_LIST, null);
    }

    /**
     * 获取当前所有选中的权限,permissionCode
     *
     * @return
     */
    private List<String> getCheckboxValue() {
        Integer col_size = pgrid.getColumns().getChildren().size();
        Integer row_size = pgrid.getRows().getChildren().size();
        List<String> chkValues = new ArrayList<>();
//        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
        for (int i = 0; i < row_size; i++) {
            if (pgrid.getCell(i, 1) == null) {
                break;
            }
            int count = pgrid.getCell(i, 1).getChildren().size();
            for (int j = 0; j < count; j++) {
                if (pgrid.getCell(i, 1) == null) {
                    break;
                }
                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
                if (checkbox.isChecked()) {
                    chkValues.add(checkbox.getValue());
                }

            }
        }
        return chkValues;
    }
}
