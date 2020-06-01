package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.RoleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色管理
 * Created by zyf on 2017/7/24.
 */
@Slf4j
@Service("roleService")
public class RoleService {

    @Autowired
    private RestClient restClient;


    public List<RoleInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/role/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<RoleItem> getPageList(PageParam<RoleItem> pageParam) {
        try {
            return restClient.getPageList("/role/getPageList", pageParam, new ParameterizedTypeReference<PageResult<RoleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public RoleInfo findOneById(String objId) {
        ResponseEntity<RoleInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/role/findOneById", requestEntity, RoleInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }


    public RoleInfo findOneWithUsersAndPermissionsById(String objId) {
        ResponseEntity<RoleInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/role/findOneWithUsersAndPermissionsById", requestEntity, RoleInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:findOneWithUsersAndPermissionsById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除角色所绑定的用户
     *
     * @param objId
     * @return
     */

    public boolean removeUsersFromRole(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.put("/role/removeUsersFromRole", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 重新绑定角色与用户的关系
     *
     * @param roleInfo
     * @return
     */

    public boolean addUsersToRole(RoleInfo roleInfo) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(roleInfo, null);
            responseEntity = restClient.post("/role/addUsersToRole", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 删除角色 与 权限 的关联关系
     *
     * @param roleId
     */

    public Boolean deleteUserWithPermission(String roleId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(roleId, null);
            responseEntity = restClient.post("/role/deleteUserWithPermission", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:deleteUserWithPermission:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 保存角色 与 权限 的关联关系
     *
     * @param roleInfo
     */

    public Boolean addUsersWithPermission(RoleInfo roleInfo) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<RoleInfo> requestEntity = new HttpEntity<>(roleInfo, null);
            responseEntity = restClient.post("/role/addUsersWithPermission", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:addUsersWithPermission:error" + e.getMessage());
            return false;
        }
    }


    public RoleInfo save(RoleInfo info) {

        ResponseEntity<RoleInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(info, null);
            responseEntity = restClient.post("/role/save", requestEntity, RoleInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:save:error" + e.getMessage());
            return null;
        }
    }


    public boolean deleteByObjId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/role/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return false;
        }
    }

}
