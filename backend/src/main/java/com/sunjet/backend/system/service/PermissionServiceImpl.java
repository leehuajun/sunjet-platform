package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.PermissionEntity;
import com.sunjet.backend.system.repository.PermissionRepository;
import com.sunjet.backend.system.repository.RoleWithPermissionRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.system.admin.PermissionInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    RoleWithPermissionRepository roleWithPermissionRepository;
    //@Autowired
    //private JdbcTemplate jdbcTemplate;


    @Override
    public boolean deleteAllByResourceCode(String code) {
        try {

            if (StringUtils.isNotBlank(code)) {
                //获取资源对应的权限
                List<PermissionEntity> pms = permissionRepository.getPermissionsByResourceCode(code);
                if (pms != null && pms.size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (PermissionEntity entity : pms) {
                        list.add(entity.getObjId());
                    }
                    //删除关联关系
                    roleWithPermissionRepository.deleteRoleWithPermission(list);
//                    roleWithPermissionRepository.dele(list);
                }

                //删除权限
                permissionRepository.deleteAllByCode(code + ":%");

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存一个实体
     *
     * @param permissionInfo
     * @return
     */
    @Override
    public PermissionInfo save(PermissionInfo permissionInfo) {
        try {
            PermissionEntity entity = permissionRepository.save(BeanUtils.copyPropertys(permissionInfo, new PermissionEntity()));
            return BeanUtils.copyPropertys(entity, permissionInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除一个实体
     *
     * @param permissionInfo
     * @return
     */
    @Override
    public boolean delete(PermissionInfo permissionInfo) {
        try {
            this.delete(permissionInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过ID删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            permissionRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId
     *
     * @param objId
     * @return
     */
    @Override
    public PermissionInfo findOne(String objId) {

        try {
            return BeanUtils.copyPropertys(permissionRepository.findOne(objId), new PermissionInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有实体
     *
     * @return
     */
    @Override
    public List<PermissionInfo> findAll() {
        List<PermissionEntity> permissionRepositoryAll = permissionRepository.findAll();
        List<PermissionInfo> permissionInfoList = new ArrayList<>();
        for (PermissionEntity entity : permissionRepositoryAll) {
            permissionInfoList.add(BeanUtils.copyPropertys(entity, new PermissionInfo()));
        }
        return permissionInfoList;
    }

    /**
     * 获取角色对应的权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<PermissionInfo> findAllByRoleId(String roleId) {

        List<PermissionEntity> permissionEntityList = permissionRepository.findAllByRoleId(roleId);
        List<PermissionInfo> permissionInfoList = null;
        if (permissionEntityList != null && permissionEntityList.size() > 0) {
            permissionInfoList = new ArrayList<>();
            for (PermissionEntity entity : permissionEntityList) {
                permissionInfoList.add(BeanUtils.copyPropertys(entity, new PermissionInfo()));
            }

        }
        return permissionInfoList;
    }

}