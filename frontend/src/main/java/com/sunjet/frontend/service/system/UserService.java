package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.admin.UserItem;
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
 * Created by SUNJET_WS on 2017/7/25.
 * 用户
 */
@Slf4j
@Service("userService")
public class UserService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<UserInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<UserInfo> userInfoList = restClient.findAll("/user/findAll", requestEntity, new ParameterizedTypeReference<List<UserInfo>>() {
            });
            return userInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("UserServicelmpl:findAll:error" + e.getMessage());
            return null;
        }
    }


    public UserInfo save(UserInfo userInfo) {
        ResponseEntity<UserInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(userInfo, null);
            responseEntity = restClient.post("/user/tosave", requestEntity, UserInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return null;
        }
    }


    public UserInfo findOne(String objId) {
        ResponseEntity<UserInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/user/findOne", requestEntity, UserInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return null;
        }
    }


    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/user/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 修改密码
     *
     * @param userInfo
     * @return
     */

    public UserInfo changePassword(UserInfo userInfo) {
        ResponseEntity<UserInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(userInfo, null);
            responseEntity = restClient.post("/user/changePassWord", requestEntity, UserInfo.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("UserServicelmpl:changePassword:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 获得用户名
     *
     * @param userId
     * @return
     */

    public String findOneWithUserId(String userId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(userId, null);
            responseEntity = restClient.get("/user/findOneWithUserId", requestEntity, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:removeUsersFromRole:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有服务经理
     *
     * @return
     */

    public List<UserInfo> findAllByRoleName(String roleName) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(roleName, null);
            List<UserInfo> userInfoList = restClient.findAll("/user/findAllByRoleName", requestEntity, new ParameterizedTypeReference<List<UserInfo>>() {
            });
            return userInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<UserItem> getPageList(PageParam<UserItem> pageParam) {
        try {

            return restClient.getPageList("/user/getPageList", pageParam, new ParameterizedTypeReference<PageResult<UserItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("UserServicelmpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过logId获取用户
     *
     * @param logId
     * @return
     */
    public UserInfo findOneByLogId(String logId) {
        ResponseEntity<UserInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(logId, null);
            responseEntity = restClient.get("/user/findOneByLogId", requestEntity, UserInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
