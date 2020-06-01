package com.sunjet.frontend.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfb on 17-7-25.
 * 权限管理
 */
@Service("permissionService")
@Slf4j
public class PermissionService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 通过资源编号删除权限
     *
     * @param resourceCode
     * @return
     */
    public boolean deleteAllByResourceCode(String resourceCode) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(resourceCode, null);
            responseEntity = restClient.delete("/permission/deleteAllByResourceCode", requestEntity, Boolean.class);
            log.info("PermissionServiceImpl:deleteAllByResourceCode:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PermissionServiceImpl:deleteAllByResourceCode:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 保存权限
     *
     * @param permissionInfo
     */
    public PermissionInfo save(PermissionInfo permissionInfo) {

        ResponseEntity<PermissionInfo> responseEntity = null;
        try {
            HttpEntity<PermissionInfo> requestEntity = new HttpEntity<>(permissionInfo, null);
            responseEntity = restClient.post("/permission/save", requestEntity, PermissionInfo.class);
            log.info("PermissionServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PermissionServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 查询所有权限列表
     *
     * @return
     */

    public List<PermissionInfo> findAll() {
        try {
            ResponseEntity<String> permissionInfoResponseEntity = restClient.get("/permission/findAll", String.class);
            List<PermissionInfo> body = (List<PermissionInfo>) mapper.readValue(permissionInfoResponseEntity.getBody(), List.class);
            List<PermissionInfo> permissionInfoList = new ArrayList<>();
            for (Object o : body) {
                JSONObject object = JSONObject.fromObject(o);
                PermissionInfo info = JsonHelper.json2Bean(object.toString(), PermissionInfo.class);
                permissionInfoList.add(info);
            }
            log.info("PermissionServiceImpl:findAll:success:");
            return permissionInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PermissionServiceImpl:findAll:error:" + e.getMessage());
            return null;
        }
    }
}
