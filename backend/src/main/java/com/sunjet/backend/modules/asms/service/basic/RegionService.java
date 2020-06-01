package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 地区
 */
public interface RegionService {

    ProvinceInfo saveProvince(ProvinceInfo provinceInfo);

    List<CityInfo> findCitiesByProvinceId(String provinceId);

    List<CountyInfo> findCountiesByCityId(String cityId);

    List<ProvinceInfo> findProvinces();

    CityInfo findCityById(String cityId);

    CountyInfo findCountyById(String countyId);

    ProvinceInfo findProvinceById(String provinceId);

    List<ProvinceInfo> findAllProvincesByAgencyId(String agencyId);

}
