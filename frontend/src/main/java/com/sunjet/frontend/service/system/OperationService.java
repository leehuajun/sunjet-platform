package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.OperationItem;
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
 * 操作列表
 * Created by zyf on 2017/7/21.
 */
@Slf4j
@Service("operationService")
public class OperationService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<OperationInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/operation/findAll", List.class);
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
    public PageResult<OperationItem> getPageList(PageParam<OperationItem> pageParam) {
        try {
            return restClient.getPageList("/operation/getPageList", pageParam, new ParameterizedTypeReference<PageResult<OperationItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */

    public OperationInfo findOneById(String objId) {
        ResponseEntity<OperationInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/operation/findOneById", requestEntity, OperationInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:findOneById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId删除
     *
     * @param objId
     */

    public boolean deleteByObjId(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/operation/deleteByObjId", requestEntity, Boolean.class);
            log.info("OperationServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param operationInfo
     * @return
     */

    public OperationInfo save(OperationInfo operationInfo) {
        ResponseEntity<OperationInfo> responseEntity = null;
        try {
            HttpEntity<OperationInfo> requestEntity = new HttpEntity<>(operationInfo, null);
            responseEntity = restClient.post("/operation/save", requestEntity, OperationInfo.class);
            log.info("OperationServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param operationInfo
     * @return
     */

    public boolean delete(OperationInfo operationInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<OperationInfo> requestEntity = new HttpEntity<>(operationInfo, null);
            responseEntity = restClient.delete("/operation/delete", requestEntity, Boolean.class);
            log.info("OperationServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("OperationServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }
}
