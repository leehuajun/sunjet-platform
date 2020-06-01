package com.sunjet.frontend.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/27.
 * 车辆信息
 */
@Slf4j
@Service("vehicleService")
public class VehicleService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    public PageResult<VehicleInfo> getPageList(PageParam<VehicleInfo> pageParam) {
        try {
            return restClient.getPageList("/vehicle/getPageList", pageParam, new ParameterizedTypeReference<PageResult<VehicleInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:getPageList:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 查询所有车辆列表
     *
     * @return
     */
    public List<VehicleInfo> findAll() {
        try {
            ResponseEntity<List> responseEntity = restClient.get("/vehicle/findAll", List.class);
            log.info("VehicleService:findAll:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId查实体
     *
     * @param objId
     * @return
     */
    public VehicleInfo findOne(String objId) {
        ResponseEntity<VehicleInfo> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/vehicle/findOne", requestEntity, VehicleInfo.class);
            log.info("VehicleService:findOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findOne:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 非首保车辆不在查询
     *
     * @param keyWord
     * @return
     */
    public List<VehicleInfo> findAllByKeywordAndFmDateIsNull(String keyWord) {

        try {
            HttpEntity requestEntity = new HttpEntity<>(keyWord, null);
            ResponseEntity<String> responseEntity = restClient.post("/vehicle/findAllByKeywordAndFmDateIsNull", requestEntity, String.class);

            List<VehicleInfo> body = (List<VehicleInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            for (Object o : body) {
                VehicleInfo vehicleInfo = JsonHelper.map2Bean(o, VehicleInfo.class);
                vehicleInfoList.add(vehicleInfo);
            }
            log.info("VehicleService:findAllByKeywordAndFmDateIsNull:success");
            return vehicleInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findAllByKeywordAndFmDateIsNull:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 条件查找实体列表集合
     *
     * @param keyword
     * @return
     */
    public List<VehicleInfo> findAllByKeyword(String keyword) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(keyword, null);
            List<VehicleInfo> vehicleInfos = restClient.findAll("/vehicle/findAllByKeyword", requestEntity, new ParameterizedTypeReference<List<VehicleInfo>>() {
            });

            //List<VehicleInfo> body = (List<VehicleInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            //List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            //for (Object o : body) {
            //    VehicleInfo vehicleInfo = JsonHelper.map2Bean(o, VehicleInfo.class);
            //    vehicleInfoList.add(vehicleInfo);
            //}
            //log.info("VehicleService:findAllByKeyword:success");
            return vehicleInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findAllByKeyword:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 保存
     *
     * @param vehicleInfo
     * @return
     */
    public VehicleInfo save(VehicleInfo vehicleInfo) {
        ResponseEntity<VehicleInfo> responseEntity = null;
        try {
            HttpEntity<VehicleInfo> requestEntity = new HttpEntity<>(vehicleInfo, null);
            responseEntity = restClient.post("/vehicle/save", requestEntity, VehicleInfo.class);

            log.info("VehicleService:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findOne:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId 查找速报车辆实体
     *
     * @param objId
     * @return
     */
    public List<VehicleInfo> findByVehicleId(List<String> objId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity<List> requestEntity = new HttpEntity<>(objId, null);

            responseEntity = restClient.get("/vehicle/findByVehicleId", requestEntity, String.class);

            List<VehicleInfo> body = (List<VehicleInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            for (Object o : body) {
                VehicleInfo vehicleInfo = JsonHelper.map2Bean(o, VehicleInfo.class);
                vehicleInfoList.add(vehicleInfo);
            }
            log.info("VehicleService:findByVehicleId:success");
            return vehicleInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findByVehicleId:error" + e.getMessage());
            return null;
        }

    }

    public List<VehicleInfo> findAllByVinIn(List<String> vins) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(vins, null);
            List<VehicleInfo> vehicleInfos = restClient.findAll("/vehicle/findAllByVinIn", requestEntity, new ParameterizedTypeReference<List<VehicleInfo>>() {
            });
            return vehicleInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findAllByVinIn:error" + e.getMessage());
            return null;
        }
    }

    public List<VehicleInfoExt> importVehicles(List<VehicleInfoExt> tmpInfos) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(tmpInfos, null);
            List<VehicleInfoExt> list = restClient.findAll("/vehicle/importVehicles", requestEntity, new ParameterizedTypeReference<List<VehicleInfoExt>>() {
            });
            log.info("VehicleService:importVehicles:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:importVehicles:error" + e.getMessage());
            return null;
        }
    }

    public List<VehicleInfoExt> modifyVehicles(List<VehicleInfoExt> tmpInfos) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(tmpInfos, null);
            List<VehicleInfoExt> list = restClient.findAll("/vehicle/modifyVehicles", requestEntity, new ParameterizedTypeReference<List<VehicleInfoExt>>() {
            });
            log.info("VehicleService:importVehicles:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:importVehicles:error" + e.getMessage());
            return null;
        }
    }

    public VehicleInfo findOneByVin(String vin) {
        ResponseEntity<VehicleInfo> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(vin, null);
            responseEntity = restClient.get("/vehicle/findOneByVin", requestEntity, VehicleInfo.class);
            log.info("VehicleService:findOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("VehicleService:findOneByVin:error" + e.getMessage());
            return null;
        }
    }

    /**
     * @param objId
     * @return
     */
    //@Override
    //public List<VehicleInfo> findAllByKeyword(String objId) {
    //    return null;
    //}


    //@Override
    //public VehicleInfo findOneByVin(String vin) {
    //    return null;
    //}
}
