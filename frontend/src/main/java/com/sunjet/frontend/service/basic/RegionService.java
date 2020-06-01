package com.sunjet.frontend.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.basic.*;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/28.
 * 地区
 */
@Slf4j
@Service("regionService")
public class RegionService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();


    /**
     * 根据 Provice 的 ID 获取 Province 对象
     *
     * @param provinceId
     * @return
     */
    public ProvinceInfo findProvinceById(String provinceId) {
        ResponseEntity<ProvinceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(provinceId, null);
            responseEntity = restClient.get("/region/findProvinceById", requestEntity, ProvinceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RegionService:findProvinceById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据 City 的 ID 获取 City 对象
     *
     * @param provinceId
     * @return
     */
    public CityInfo findCityById(String provinceId) {
        ResponseEntity<CityInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(provinceId, null);
            responseEntity = restClient.get("/region/findCityById", requestEntity, CityInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RegionService:findCityById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据 County 的 ID 获取 County 对象
     *
     * @param countyId
     * @return
     */
    public CountyInfo findCountyById(String countyId) {
        ResponseEntity<CountyInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(countyId, null);
            responseEntity = restClient.get("/region/findCountyById", requestEntity, CountyInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RegionService:findCountyById:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取所有的 City 对象
     *
     * @return
     */
    public List<CityInfo> findAllCity() {
        return null;
    }

    /**
     * 获取所有的 County 对象
     *
     * @return
     */
    public List<CountyInfo> findAllCounty() {
        return null;
    }

    /**
     * 根据 City 的 ID 获取所有 County 对象
     *
     * @param cityId
     * @return
     */
    public List<CountyInfo> findCountiesByCityId(String cityId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(cityId, null);
            List<CountyInfo> countyInfoList = restClient.findAll("/region/findCountiesByCityId", requestEntity, new ParameterizedTypeReference<List<CountyInfo>>() {
            });

            log.info("RegionService:findCountiesByCityId:success");
            return countyInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RegionService:findCountiesByCityId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据 Province 的 ID 获取所有 City 对象
     *
     * @param provinceId
     * @return
     */
    public List<CityInfo> findCitiesByProvinceId(String provinceId) {

        try {
            HttpEntity requestEntity = new HttpEntity<>(provinceId, null);
            List<CityInfo> cityInfoList = restClient.findAll("/region/findCitiesByProvinceId", requestEntity, new ParameterizedTypeReference<List<CityInfo>>() {
            });
            log.info("RegionService:findCitiesByProvinceId:success");
            return cityInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RegionService:findCitiesByProvinceId:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 查找所有省份
     *
     * @return
     */
    public List<ProvinceInfo> findAllProvince() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<ProvinceInfo> provinceInfos = restClient.findAll("/region/findAllProvince", requestEntity, new ParameterizedTypeReference<List<ProvinceInfo>>() {
            });
            log.info("ProvinceServiceImpl:findAll:success");
            return provinceInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ProvinceServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 新增省份
     *
     * @param provinceInfo
     * @return
     */
    public ProvinceInfo saveProvince(ProvinceInfo provinceInfo) {
        ResponseEntity<ProvinceInfo> responseEntity = null;
        try {
            HttpEntity<ProvinceInfo> requestEntity = new HttpEntity<>(provinceInfo, null);
            responseEntity = restClient.post("/region/saveProvince", requestEntity, ProvinceInfo.class);
            log.info("ProvinceServiceImpl:saveProvince:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ProvinceServiceImpl:saveProvince:error" + e.getMessage());
            return null;
        }
    }


}
