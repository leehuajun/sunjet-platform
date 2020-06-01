package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.service.basic.RegionService;
import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/27.
 */
@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping("/findAllProvince")
    public List<ProvinceInfo> findAll() {
        return regionService.findProvinces();
    }

    @PostMapping("/findCitiesByProvinceId")
    public List<CityInfo> findCitiesByProvinceId(@RequestBody String ProvinceId) {
        return regionService.findCitiesByProvinceId(ProvinceId);
    }

    @PostMapping("/findCountiesByCityId")
    public List<CountyInfo> findCountiesByCityId(@RequestBody String cityId) {
        return regionService.findCountiesByCityId(cityId);
    }

    @PostMapping("/findProvinceById")
    public ProvinceInfo findProvinceById(@RequestBody String provinceId) {
        return regionService.findProvinceById(provinceId);
    }

    @PostMapping("/findCityById")
    public CityInfo findCityById(@RequestBody String cityId) {
        return regionService.findCityById(cityId);
    }

    @PostMapping("/findCountyById")
    public CountyInfo findCountyById(@RequestBody String countyId) {
        return regionService.findCountyById(countyId);
    }

    @PostMapping("/saveProvince")
    public ProvinceInfo saveProvince(@RequestBody ProvinceInfo provinceInfo) {
        return regionService.saveProvince(provinceInfo);
    }

}
