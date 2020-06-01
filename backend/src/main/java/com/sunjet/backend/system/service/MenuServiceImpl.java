package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.MenuEntity;
import com.sunjet.backend.system.repository.MenuRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.RoleInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 菜单实现类
 */
@Transactional
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * save一个实体
     *
     * @param menuInfo
     * @return
     */
    @Override
    public MenuInfo save(MenuInfo menuInfo) {
        try {
            MenuEntity entity = menuRepository.save(infoToEntity(menuInfo));
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过一个实体删除
     *
     * @param menuInfo
     * @return
     */
    @Override
    public boolean delete(MenuInfo menuInfo) {
        try {
            menuRepository.delete(infoToEntity(menuInfo));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过一个objId删除
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            menuRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 通过objId查一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public MenuInfo findOne(String objId) {
        try {
            return entityToInfo(menuRepository.findOne(objId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有菜单
     *
     * @return
     */
    @Override
    public List<MenuInfo> findAll() {
        // 获得数据源菜单
        List<MenuEntity> menuEntityList = menuRepository.findAll();

        List<MenuInfo> menuInfoList = new ArrayList<MenuInfo>();

        //转换为info数据源菜单
        for (MenuEntity menuEntity : menuEntityList) {
            menuInfoList.add(entityToInfo(menuEntity));
        }


        return menuInfoList;
    }

    /**
     * 根据角色获取角色对应的菜单
     *
     * @param roleInfoList
     * @return
     */
    @Override
    public List<MenuInfo> findAllByRoles(List<RoleInfo> roleInfoList) {

        List<MenuInfo> menuInfoList = null;
        if (roleInfoList != null && roleInfoList.size() > 0) {
            ArrayList<String> ids = new ArrayList<>();

            for (RoleInfo info : roleInfoList) {
                List<PermissionInfo> permissionInfoList = info.getPermissions();
                for (PermissionInfo permissionInfo : permissionInfoList) {
                    if (permissionInfo != null) {
                        ids.add(permissionInfo.getPermissionCode());
                    }
                }
            }

            // 获得数据源菜单
            List<MenuEntity> menuEntityList = menuRepository.findAllByCodes(ids);
            //获取父级菜单
            List<MenuEntity> menuParentList = menuRepository.findParentsByCodes(ids);
            //添加父级菜单
            menuEntityList.addAll(menuParentList);

            menuInfoList = new ArrayList<MenuInfo>();

            //转换为info数据源菜单
            for (MenuEntity menuEntity : menuEntityList) {
                menuInfoList.add(entityToInfo(menuEntity));
            }
        }

        return menuInfoList;
    }

    /**
     * 通过URL查菜单
     *
     * @param url
     * @return
     */
    @Override
    public MenuInfo findMenuByUrl(String url) {
        MenuEntity menuEntity = menuRepository.findMenuByUrl(url);
        MenuInfo menuInfo = BeanUtils.copyPropertys(menuEntity, new MenuInfo());
        menuInfo.setObjId(menuEntity.getObjId());
        return menuInfo;
    }


    /**
     * 将info 转换为Entity
     *
     * @param menuInfo info
     * @return entity
     */
    private MenuEntity infoToEntity(MenuInfo menuInfo) {
        MenuEntity parent = null;
        if (menuInfo.getParent() != null) {
            parent = infoToEntity(menuInfo.getParent());
        }
        return MenuEntity.MenuEntityBuilder
                .aMenuEntity()
                .withObjId(menuInfo.getObjId() == null ? null : menuInfo.getObjId())
                .withEnabled(menuInfo.getEnabled())
                .withName(menuInfo.getName())
                .withCreaterName(menuInfo.getCreaterName())
                .withModifierName(menuInfo.getModifierName())
                .withUrl(menuInfo.getUrl())
                .withIcon(menuInfo.getIcon())
                .withSeq(menuInfo.getSeq())
                .withOpen(menuInfo.getOpen())
                .withPermissionCode(menuInfo.getPermissionCode())
                .withPermissionName(menuInfo.getPermissionName())
                .withParent(parent)
                .build();
    }

    /**
     * 将Entity 转换为info
     *
     * @param menuEntity
     * @return
     */
    private MenuInfo entityToInfo(MenuEntity menuEntity) {
        MenuInfo parent = null;
        if (menuEntity.getParent() != null) {
            parent = entityToInfo(menuEntity.getParent());
        }

        return MenuInfo.MenuInfoBuilder
                .aMenuInfo()
                .withObjId(menuEntity.getObjId())
                .withEnabled(menuEntity.getEnabled())
                .withName(menuEntity.getName())
                .withCreaterName(menuEntity.getCreaterName())
                .withUrl(menuEntity.getUrl())
                .withIcon(menuEntity.getIcon())
                .withSeq(menuEntity.getSeq())
                .withOpen(menuEntity.getOpen())
                .withPermissionCode(menuEntity.getPermissionCode())
                .withPermissionName(menuEntity.getPermissionName())
                .withParent(parent)
                .build();
    }


}