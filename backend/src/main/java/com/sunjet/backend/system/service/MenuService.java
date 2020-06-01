package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.RoleInfo;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 菜单
 */
public interface MenuService {

    MenuInfo save(MenuInfo menuInfo);

    boolean delete(MenuInfo menuInfo);

    boolean delete(String objId);

    MenuInfo findOne(String objId);

    List<MenuInfo> findAll();

    /**
     * 根据角色获取角色对应的菜单
     *
     * @param roleInfoList
     * @return
     */
    public List<MenuInfo> findAllByRoles(List<RoleInfo> roleInfoList);


    MenuInfo findMenuByUrl(String url);
}
