package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.CityEntity;
import com.sunjet.backend.modules.asms.entity.basic.CountyEntity;
import com.sunjet.backend.modules.asms.entity.basic.ProvinceEntity;
import com.sunjet.backend.modules.asms.repository.basic.CityRepository;
import com.sunjet.backend.modules.asms.repository.basic.CountyRepository;
import com.sunjet.backend.modules.asms.repository.basic.ProvinceRepository;
import com.sunjet.backend.modules.asms.repository.basic.RegionRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 地区
 */
@Transactional
@Service("regionService")
public class RegionServiceImpl implements RegionService {

    @Autowired
    ProvinceRepository provinceRepository;     //省份
    @Autowired
    private RegionRepository regionRepository;      //地区
    @Autowired
    private CityRepository cityRepository;    //城市

    @Autowired
    private CountyRepository countyRepository;     //县


    @Override
    public ProvinceInfo saveProvince(ProvinceInfo provinceInfo) {
        ProvinceEntity entity = BeanUtils.copyPropertys(provinceInfo, new ProvinceEntity());
        return BeanUtils.copyPropertys(regionRepository.save(entity), new ProvinceInfo());
    }

    /**
     * 通过ProvinceId 查所有city
     *
     * @param provinceId
     * @return
     */
    @Override
    public List<CityInfo> findCitiesByProvinceId(String provinceId) {
        List<CityEntity> cityEntityList = cityRepository.findAllByProvinceId(provinceId);
        List<CityInfo> cityInfoList = new ArrayList<>();
        for (CityEntity cityEntity : cityEntityList) {
            cityInfoList.add(BeanUtils.copyPropertys(cityEntity, new CityInfo()));
        }
        return cityInfoList;
    }

    /**
     * 通过 cityId 查所有county
     *
     * @param cityId
     * @return
     */
    @Override
    public List<CountyInfo> findCountiesByCityId(String cityId) {
        try {
            List<CountyEntity> countyEntityList = countyRepository.findAllByCityId(cityId);
            List<CountyInfo> countyInfoList = new ArrayList<>();
            for (CountyEntity countyEntity : countyEntityList) {
                countyInfoList.add(BeanUtils.copyPropertys(countyEntity, new CountyInfo()));
            }
            return countyInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ProvinceInfo> findProvinces() {
        try {
            List<ProvinceEntity> list = provinceRepository.findAll();
            List<ProvinceInfo> infos = new ArrayList<>();
            for (ProvinceEntity provinceEntity : list) {
                infos.add(BeanUtils.copyPropertys(provinceEntity, new ProvinceInfo()));
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CityInfo findCityById(String cityId) {
        try {
            CityEntity entity = cityRepository.findOne(cityId);
            return BeanUtils.copyPropertys(entity, new CityInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CountyInfo findCountyById(String countyId) {
        try {
            CountyEntity entity = countyRepository.findOne(countyId);
            return BeanUtils.copyPropertys(entity, new CountyInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProvinceInfo findProvinceById(String provinceId) {
        try {
            ProvinceEntity provinceEntity = provinceRepository.findOne(provinceId);
            return BeanUtils.copyPropertys(provinceEntity, new ProvinceInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取合作商覆盖的省份
     *
     * @param agencyId
     * @return
     */
    @Override
    public List<ProvinceInfo> findAllProvincesByAgencyId(String agencyId) {

        List<ProvinceEntity> regionEntityList = regionRepository.findAllProvincesByAgencyId(agencyId);
        List<ProvinceInfo> regionInfoList = null;
        if (regionEntityList != null && regionEntityList.size() > 0) {
            regionInfoList = new ArrayList<>();
            for (ProvinceEntity entity : regionEntityList) {
                regionInfoList.add(BeanUtils.copyPropertys(entity, new ProvinceInfo()));
            }

        }
        return regionInfoList;
    }
}
