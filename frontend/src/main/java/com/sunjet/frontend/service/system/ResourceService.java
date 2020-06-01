package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.dto.system.admin.ResourceItem;
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
 * Created by wfb on 17-7-21.
 * 资源管理
 */
@Service("resourceService")
@Slf4j
public class ResourceService {

    @Autowired
    private RestClient restClient;

    /**
     * 新增
     *
     * @param resourceInfo
     * @return
     */

    public ResourceInfo save(ResourceInfo resourceInfo) {
        ResponseEntity<ResourceInfo> responseEntity = null;
        try {
            HttpEntity<ResourceInfo> requestEntity = new HttpEntity<>(resourceInfo, null);
            responseEntity = restClient.post("/resource/save", requestEntity, ResourceInfo.class);
            log.info("ResourceServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<ResourceInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/resource/findAll", List.class);
            log.info("ResourceServiceImpl:findAll:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:findAll:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<ResourceItem> getPageList(PageParam<ResourceItem> pageParam) {
        try {
            return restClient.getPageList("/resource/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ResourceItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }


    /**
     * 删除
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/resource/delete", requestEntity, Boolean.class);
            log.info("ResourceServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过操作id获取资源
     *
     * @param objId
     * @return
     */

    public ResourceInfo findOneWithOperationsById(String objId) {
        ResponseEntity<ResourceInfo> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/resource/findOneWithOperationsById", requestEntity, ResourceInfo.class);
            log.info("ResourceServiceImpl:findOneWithOperationsById:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:findOneWithOperationsById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过操作获取资源集合
     *
     * @return
     */

    public List<ResourceInfo> findAllWithOperations() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/resource/findAllWithOperations", List.class);
            log.info("ResourceServiceImpl:findAllWithOperations:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:findAllWithOperations:error:" + e.getMessage());
            return null;
        }
    }
}
