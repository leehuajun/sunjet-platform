package com.sunjet.backend.modules.asms.service.basic;

import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfoExt;
import com.sunjet.dto.asms.basic.VehicleModelInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface VehicleService {

    List<VehicleInfo> findAllByKeyword(String keyword);

    List<VehicleInfo> findAllByKeywordAndFmDateIsNull(String keyWord);

    VehicleInfo findOneByVin(String vin);


    VehicleInfo save(VehicleInfo iconsInfo);

    boolean delete(VehicleInfo iconsInfo);

    boolean delete(String objId);

    VehicleInfo findOne(String objId);

    PageResult<VehicleInfo> getPageList(PageParam<VehicleInfo> pageParam);

    List<VehicleInfo> findByVehicleId(ArrayList<String> objIds);

    List<VehicleInfo> findAllByVinIn(List<String> vins);

    List<VehicleInfoExt> importVehicles(List<VehicleInfoExt> vehicles);

    List<VehicleInfoExt> modifyVehicles(List<VehicleInfoExt> vehicles);

}
