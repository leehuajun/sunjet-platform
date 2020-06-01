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
public interface VehicleModelService {


    VehicleModelInfo save(VehicleModelInfo info);

    boolean delete(String objId);

    VehicleModelInfo findOne(String objId);

    List<VehicleModelInfo> findAll();

}
