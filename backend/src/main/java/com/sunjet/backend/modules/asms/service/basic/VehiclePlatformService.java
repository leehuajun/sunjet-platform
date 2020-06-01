package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.dto.asms.basic.VehiclePlatformInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface VehiclePlatformService {
    VehiclePlatformInfo save(VehiclePlatformInfo info);

    boolean delete(String objId);

    VehiclePlatformInfo findOne(String objId);

    List<VehiclePlatformInfo> findAll();
}
