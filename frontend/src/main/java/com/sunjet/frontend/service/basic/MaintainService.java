package com.sunjet.frontend.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainInfoExt;
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
 * Created by hhn on 2017/7/27.
 */
@Slf4j
@Service("maintainService")
public class MaintainService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取列表集合
     *
     * @return
     */
    public List<MaintainInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/maintain/findAll", List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<MaintainInfo> getPageList(PageParam<MaintainInfo> pageParam) {
        try {
            //
            //HttpEntity<PageParam> requestEntity = new HttpEntity<>(pageParam, null);
            //
            //ResponseEntity<Object> responseEntity = restClient.get(restClient.getPath("/maintain/getPageList"),requestEntity,Object.class);
            //
            //log.info("MaintainService:getPageList:success");
            //
            //LinkedHashMap map = (LinkedHashMap)responseEntity.getBody();
            //
            //PageResult<MaintainInfo> pageResult = new PageResult<>(map);
            //
            //return pageResult;
            return restClient.getPageList("/maintain/getPageList", pageParam, new ParameterizedTypeReference<PageResult<MaintainInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */
    public MaintainInfo findOneById(String objId) {
        ResponseEntity<MaintainInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.post("/maintain/findOneById", requestEntity, MaintainInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:findOneById:error" + e.getMessage());
            return null;
        }
    }

    ///**
    // * 项目工时定额
    // *
    // * @param keyword
    // * @return
    // */
    //public List<MaintainInfo> findAllByFilter(String keyword) {
    //    MaintainInfo maintainInfo = new MaintainInfo();
    //    maintainInfo.setCode(keyword);
    //    try {
    //        HttpEntity requestEntity = new HttpEntity<>(maintainInfo, null);
    //        //ResponseEntity<String> responseEntity = restClient.post("/maintain/findAllByFilter", requestEntity, String.class);
    //        //List<MaintainInfo> body = (List<MaintainInfo>) mapper.readValue(responseEntity.getBody(), List.class);
    //        //List<MaintainInfo> maintainInfoList = new ArrayList<>();
    //        //for (Object o : body) {
    //        //    MaintainInfo maintainInfo = JsonHelper.map2Bean(o, MaintainInfo.class);
    //        //    maintainInfoList.add(maintainInfo);
    //        //}
    //        List<MaintainInfo> list = restClient.findAll("/maintain/findAllByFilter", requestEntity, new ParameterizedTypeReference<List<MaintainInfo>>() {
    //        });
    //        log.info("MaintainService:findAllByFilter:success");
    //        return list;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        log.info("MaintainService:findAllByFilter:error" + e.getMessage());
    //        return null;
    //    }
    //}

    public List<MaintainInfoExt> importMaintains(List<MaintainInfoExt> tmpInfos) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(tmpInfos, null);
            List<MaintainInfoExt> list = restClient.findAll("/maintain/importData", requestEntity, new ParameterizedTypeReference<List<MaintainInfoExt>>() {
            });
            log.info("MaintainService:importMaintains:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:importMaintains:error" + e.getMessage());
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
            responseEntity = restClient.delete("/maintain/deleteByObjId", requestEntity, Boolean.class);
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
     * @param maintainInfo
     * @return
     */
    public MaintainInfo save(MaintainInfo maintainInfo) {
        ResponseEntity<MaintainInfo> responseEntity = null;
        try {
            HttpEntity<MaintainInfo> requestEntity = new HttpEntity<>(maintainInfo, null);
            responseEntity = restClient.post("/maintain/save", requestEntity, MaintainInfo.class);
            log.info("MaintainService:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param maintainInfo
     * @return
     */
    public boolean delete(MaintainInfo maintainInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<MaintainInfo> requestEntity = new HttpEntity<>(maintainInfo, null);
            responseEntity = restClient.delete("/maintain/delete", requestEntity, Boolean.class);
            log.info("MaintainService:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 查询维修项目列表
     *
     * @param searchmaintainInfo
     * @return
     */
    public List<MaintainInfo> findAllByFilter(MaintainInfo searchmaintainInfo) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(searchmaintainInfo, null);
            List<MaintainInfo> list = restClient.findAll("/maintain/findAllByFilter", requestEntity, new ParameterizedTypeReference<List<MaintainInfo>>() {
            });
            log.info("MaintainService:findAllByFilter:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MaintainService:findAllByFilter:error" + e.getMessage());
            return null;
        }

    }
}
