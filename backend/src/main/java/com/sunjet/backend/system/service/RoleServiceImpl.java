package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.PermissionEntity;
import com.sunjet.backend.system.entity.RoleEntity;
import com.sunjet.backend.system.entity.RoleWithPermissionEntity;
import com.sunjet.backend.system.entity.UserWithRoleEntity;
import com.sunjet.backend.system.entity.view.RoleView;
import com.sunjet.backend.system.repository.*;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.RoleItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 角色管理
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleViewRepository roleViewRepository;
    @Autowired
    private UserViewRepository userViewRepository;
    @Autowired
    private PermissionViewRepository permissionViewRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserWithRoleRepository userWithRoleRepository;
    @Autowired
    private RoleWithPermissionRepository roleWithPermissionRepository;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<RoleInfo> findAll() {
        try {
            List<RoleEntity> list = this.roleRepository.findAll();
            List<RoleInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (RoleEntity roleEntity : list) {
                    infos.add(BeanUtils.copyPropertys(roleEntity, new RoleInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存一个实体
     *
     * @param roleInfo
     * @return返回一个info
     */
    @Override
    public RoleInfo save(RoleInfo roleInfo) {
        try {
            //保存信息
            RoleEntity entity = roleRepository.save(BeanUtils.copyPropertys(roleInfo, new RoleEntity()));
            roleInfo = BeanUtils.copyPropertys(entity, roleInfo);

            //2.删除角色所绑定的用户
            this.removeUsersFromRole(roleInfo.getObjId());

            //3.重新绑定用户
            this.addUsersToRole(roleInfo);

            //4.删除角色 与 权限 的关联关系
            this.deleteUserWithPermission(roleInfo.getObjId());

            //5.保存角色 与 权限 的关联关系
            this.addUsersWithPermission(roleInfo);

            return roleInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除角色 与 权限 的关联关系
     *
     * @param roleId
     */
    public Boolean deleteUserWithPermission(String roleId) {
        try {
            //删除绑定
            roleWithPermissionRepository.deleteRoleWithPermissionByRoleId(roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存角色 与 权限 的关联关系
     *
     * @param roleInfo
     */
    public Boolean addUsersWithPermission(RoleInfo roleInfo) {
        try {
            //重新绑定
            if (roleInfo != null && roleInfo.getPermissions() != null && roleInfo.getPermissions().size() > 0) {
                List<RoleWithPermissionEntity> entityList = new ArrayList<>();
                for (PermissionInfo info : roleInfo.getPermissions()) {
                    RoleWithPermissionEntity entity = new RoleWithPermissionEntity();
                    entity.setRoleId(roleInfo.getObjId());
                    entity.setPermissionId(info.getObjId());
                    entityList.add(entity);
                }
                roleWithPermissionRepository.save(entityList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过roleId查角色
     *
     * @param roleId
     * @return
     */
    @Override
    public RoleEntity findOneByRoleId(String roleId) {
        return roleRepository.findOneByRoleId(roleId);
    }

    @Override
    public RoleInfo findOneById(String objId) {
        try {
            RoleEntity entity = roleRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new RoleInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过角色id获取对应的权限
     *
     * @param roleId
     * @return
     */
    @Override
    public RoleInfo findOne(String roleId) {
        try {
            RoleEntity entity = roleRepository.findOne(roleId);
            return BeanUtils.copyPropertys(entity, new RoleInfo());
            //return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除用户与角色的关联关系
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean removeUsersFromRole(String roleId) {
        try {
            userWithRoleRepository.deleteByRole(roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<RoleItem> getPageList(PageParam<RoleItem> pageParam) {

        //1.查询条件
        RoleItem roleItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RoleView> specification = null;
        if (roleItem != null) {
            specification = Specifications.<RoleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(roleItem.getName()), "name", "%" + roleItem.getName() + "%")
                    .build();
        }

        //3.执行查询
        Page<RoleView> pages = roleViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //角色对应的用户
        HashMap<String, String> users = getUsersByRoles(getRoleIds(pages.getContent()));
        //角色对应的操作权限
        HashMap<String, String> permissions = getPermissionsByRoles(getRoleIds(pages.getContent()));

        //4.数据转换
        List<RoleItem> rows = new ArrayList<>();
        for (RoleView view : pages.getContent()) {
            RoleItem item = new RoleItem();
            item = BeanUtils.copyPropertys(view, item);
            //绑定角色对应的用户
            item.setUsers(users.get(item.getObjId()));
            //绑定角色对应的操作权限
            item.setPermissions(permissions.get(item.getObjId()));

            rows.add(item);
        }

        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 获取角色ids
     *
     * @param viewList
     * @return
     */
    private ArrayList<String> getRoleIds(List<RoleView> viewList) {
        ArrayList<String> ids = null;
        if (viewList != null && viewList.size() > 0) {
            ids = new ArrayList<>();
            for (RoleView roleView : viewList) {
                ids.add(roleView.getObjId());
            }
        }
        return ids;
    }

    /**
     * 获取角色对应的用户
     *
     * @param roleIds
     * @return
     */
    private HashMap<String, String> getUsersByRoles(ArrayList<String> roleIds) {
        HashMap<String, String> map = null;
        if (roleIds != null && roleIds.size() > 0) {
            List<Object> users = userViewRepository.findUsersByRoleIds(roleIds);
            map = new HashMap<>();
            for (String objId : roleIds) {

                String roleNames = "";
                for (int i = 0; i < users.size(); i++) {
                    Object[] m = (Object[]) users.get(i);
                    String id = m[0].toString();
                    if (id.equalsIgnoreCase(objId)) {
                        if (StringUtils.isNotBlank(roleNames)) {
                            roleNames += ",";
                        }
                        Object name = m[1];
                        roleNames += name == null ? "" : name.toString();
                    }
                }
                map.put(objId, roleNames);
            }
        }
        return map;
    }

    /**
     * 获取角色对应的操作权限
     *
     * @param roleIds
     * @return
     */
    private HashMap<String, String> getPermissionsByRoles(ArrayList<String> roleIds) {
        HashMap<String, String> map = null;
        if (roleIds != null && roleIds.size() > 0) {
            List<Object> users = permissionViewRepository.findPermissionsByRoleIds(roleIds);
            map = new HashMap<>();
            for (String objId : roleIds) {

                String roleNames = "";
                for (int i = 0; i < users.size(); i++) {
                    Object[] m = (Object[]) users.get(i);
                    String id = m[0].toString();
                    if (id.equalsIgnoreCase(objId)) {
                        if (StringUtils.isNotBlank(roleNames)) {
                            roleNames += ",";
                        }
                        Object resourceName = m[1];
                        Object accessName = m[2];

                        roleNames += resourceName == null ? "" : resourceName.toString();
                        roleNames += "-->";
                        roleNames += accessName == null ? "" : accessName.toString();
                    }
                }
                map.put(objId, roleNames);
            }
        }
        return map;
    }


    /**
     * 绑定 用户 与 角色 的关联关系
     *
     * @param roleInfo
     * @return
     */
    @Override
    public boolean addUsersToRole(RoleInfo roleInfo) {
        try {
            if (roleInfo != null && roleInfo.getUserInfos() != null && roleInfo.getUserInfos().size() > 0) {

                List<UserWithRoleEntity> userWithRoleEntityList = new ArrayList<>();
                for (UserInfo userInfo : roleInfo.getUserInfos()) {

                    UserWithRoleEntity entity = new UserWithRoleEntity();

                    entity.setRoleId(roleInfo.getObjId());//角色id
                    entity.setUserId(userInfo.getObjId());//用户id

                    userWithRoleEntityList.add(entity);
                }

                userWithRoleRepository.save(userWithRoleEntityList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取 用户 所关联的 角色
     *
     * @param userId
     * @return
     */
    public List<RoleInfo> findAllByUserId(String userId) {

        List<RoleEntity> entityList = roleRepository.findAllByUserId(userId);

        HashMap<String, List<PermissionInfo>> map = getPermissionInfosMap(entityList);

        List<RoleInfo> infoList = null;
        //转换
        if (entityList != null && entityList.size() > 0) {
            infoList = new ArrayList<>();
            for (RoleEntity entity : entityList) {

                RoleInfo info = BeanUtils.copyPropertys(entity, new RoleInfo());

                info.setPermissions(map.get(info.getObjId()));

                infoList.add(info);
            }
        }

        return infoList;
    }


    /**
     * 获取角色与权限的关联数据
     *
     * @param roleList
     * @return
     */
    private HashMap<String, List<PermissionInfo>> getPermissionInfosMap(List<RoleEntity> roleList) {

        ArrayList<String> roleIds = getIds(roleList);

        //1.获取所有角色对应的权限
        HashMap<String, PermissionInfo> permissionInfoMap = getPermissionInfoMap(roleIds);

        //2.获取角色与权限的关联关系
        List<RoleWithPermissionEntity> roleWithPermissionEntityList = getRoleWithPermissionEntityList(roleIds);

        //3.统一实例化角色的 List<PermissionInfo>
        HashMap<String, List<PermissionInfo>> permissionInfoListMap = getPermissionInfoListMap(roleIds);

        if (roleWithPermissionEntityList != null && roleWithPermissionEntityList.size() > 0) {
            for (RoleWithPermissionEntity with : roleWithPermissionEntityList) {

                String roleId = with.getRoleId();
                String permissionId = with.getPermissionId();

                //给已实例化的集合添加数据
                List<PermissionInfo> list = permissionInfoListMap.get(roleId);
                //从map中取权限实体
                list.add(permissionInfoMap.get(permissionId));
            }
        }

        return permissionInfoListMap;
    }

    private List<RoleWithPermissionEntity> getRoleWithPermissionEntityList(ArrayList<String> roleIds) {
        if (roleIds != null && roleIds.size() > 0) {
            return roleWithPermissionRepository.findAllByRoleIds(roleIds);
        } else {
            return null;
        }
    }

    private ArrayList<String> getIds(List<RoleEntity> roleList) {
        ArrayList<String> ids = new ArrayList<>();
        for (RoleEntity roleEntity : roleList) {
            ids.add(roleEntity.getObjId());
        }
        return ids;
    }

    /**
     * 获取角色对应的权限并转换为info map返回
     *
     * @param roleIds
     * @return
     */
    private HashMap<String, PermissionInfo> getPermissionInfoMap(ArrayList<String> roleIds) {
        HashMap<String, PermissionInfo> map = null;
        if (roleIds != null && roleIds.size() > 0) {
            map = new HashMap<>();
            List<PermissionEntity> permissionEntityList = permissionRepository.findAllByRoleIds(roleIds);
            for (PermissionEntity entity : permissionEntityList) {
                map.put(entity.getObjId(), BeanUtils.copyPropertys(entity, new PermissionInfo()));
            }
        }
        return map;
    }


    /**
     * 统一实例化角色的 List<PermissionInfo>
     *
     * @param roleIds
     * @return
     */
    private HashMap<String, List<PermissionInfo>> getPermissionInfoListMap(ArrayList<String> roleIds) {

        HashMap<String, List<PermissionInfo>> map = new HashMap<>();

        for (String roleId : roleIds) {
            List<PermissionInfo> infoList = new ArrayList<>();
            map.put(roleId, infoList);
        }
        return map;
    }

    /**
     * 删除实体
     *
     * @param roleInfo
     * @return结果
     */
    @Override
    public boolean delete(RoleInfo roleInfo) {
        try {
            delete(roleInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId删除实体
     *
     * @param objId
     * @return返回实体
     */
    @Override
    public boolean delete(String objId) {
        try {
            //删除 角色 与 用户 的关联关系
            userWithRoleRepository.deleteByRole(objId);
            //删除 角色 与 权限 的关联关系
            roleWithPermissionRepository.deleteRoleWithPermissionByRoleId(objId);
            //删除实体
            roleRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
