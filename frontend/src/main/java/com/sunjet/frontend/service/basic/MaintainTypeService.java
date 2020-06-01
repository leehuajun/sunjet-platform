package com.sunjet.frontend.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hhn on 2017/7/27.
 */
@Slf4j
@Service("maintainTypeService")
public class MaintainTypeService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取列表集合
     *
     * @return
     */
    public List<MaintainTypeInfo> findAll() {
        try {
            List<MaintainTypeInfo> infos = restClient.findAll("/maintainType/findAll", null, new ParameterizedTypeReference<List<MaintainTypeInfo>>() {
            });
//            return restClient.findAll("/goOuts/findAllByWarrantyMaintenanceObjId", requestEntity, new ParameterizedTypeReference<List<GoOutInfo>>() {

            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findAll:error" + e.getMessage());
            return null;
        }
    }

//    /**
//     * 分页
//     *
//     * @return
//     */
//    public PageResult<MaintainTypeInfo> getPageList(PageParam<MaintainTypeInfo> pageParam) {
//        try {
//            //
//            //HttpEntity<PageParam> requestEntity = new HttpEntity<>(pageParam, null);
//            //
//            //ResponseEntity<Object> responseEntity = restClient.get(restClient.getPath("/maintain/getPageList"),requestEntity,Object.class);
//            //
//            //log.info("MaintainService:getPageList:success");
//            //
//            //LinkedHashMap map = (LinkedHashMap)responseEntity.getBody();
//            //
//            //PageResult<MaintainInfo> pageResult = new PageResult<>(map);
//            //
//            //return pageResult;
//            return restClient.getPageList("/maintain/getPageList", pageParam,new ParameterizedTypeReference<PageResult<MaintainTypeInfo>>(){});
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("MaintainTypeService:getPageList:error:" + e.getMessage());
//            return null;
//        }
//
//    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */
    public MaintainTypeInfo findOne(String objId) {
        ResponseEntity<MaintainTypeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/maintainType/findOne", requestEntity, MaintainTypeInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findOne:error" + e.getMessage());
            return null;
        }
    }

    public List<MaintainTypeInfo> findVehicleModels() {
        List<MaintainTypeInfo> infos = null;
        try {
//            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            infos = restClient.findAll("/maintainType/findModels", null, new ParameterizedTypeReference<List<MaintainTypeInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findOneById:error" + e.getMessage());
            return null;
        }
    }

    public List<MaintainTypeInfo> findVehicleSystems(String parentId) {
        List<MaintainTypeInfo> infos = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(parentId, null);
            infos = restClient.findAll("/maintainType/findSystems", requestEntity, new ParameterizedTypeReference<List<MaintainTypeInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findAll:error" + e.getMessage());
            return null;
        }
    }

    public List<MaintainTypeInfo> findVehicleSubSystems(String parentId) {
        List<MaintainTypeInfo> infos = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(parentId, null);
            infos = restClient.findAll("/maintainType/findSubSystems", requestEntity, new ParameterizedTypeReference<List<MaintainTypeInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findAll:error" + e.getMessage());
            return null;
        }
    }

    public List<MaintainTypeInfo> findAllByParentId(String parentId) {
        List<MaintainTypeInfo> infos = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(parentId, null);
            infos = restClient.findAll("/maintainType/findAllByParentId", requestEntity, new ParameterizedTypeReference<List<MaintainTypeInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:findAll:error" + e.getMessage());
            return null;
        }
    }

    public List<MaintainTypeInfo> findParents(String parentId) {
        List<MaintainTypeInfo> infos = null;
        try {
            MaintainTypeInfo info = findOne(parentId);
            if (StringUtils.isNotBlank(info.getParentId())) {    // 不是顶级
                infos = findAllByParentId(info.getParentId());
            } else {
                infos = findVehicleModels();
            }
            return infos;

//            HttpEntity requestEntity = new HttpEntity<>(parentId, null);
//            infos = restClient.findAll("/maintainType/findAllByParentId", requestEntity,new ParameterizedTypeReference<List<MaintainTypeInfo>>(){});
//            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("findParents:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId删除
     *
     * @param objId
     */
    public boolean delete(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/maintainType/delete", requestEntity, Boolean.class);
            log.info("MaintainService:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param maintainTypeInfo
     * @return
     */
    public MaintainTypeInfo save(MaintainTypeInfo maintainTypeInfo) {
        ResponseEntity<MaintainTypeInfo> responseEntity = null;
        try {
            HttpEntity<MaintainTypeInfo> requestEntity = new HttpEntity<>(maintainTypeInfo, null);
            responseEntity = restClient.post("/maintainType/save", requestEntity, MaintainTypeInfo.class);
            log.info("MaintainTypeService:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainTypeService:save:error:" + e.getMessage());
            return null;
        }
    }
}
