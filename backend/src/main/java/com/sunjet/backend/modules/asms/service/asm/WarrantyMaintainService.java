package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintainEntity;
import com.sunjet.dto.asms.asm.WarrantyMaintainInfo;

import java.util.List;

/**
 * 维修项目
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintainService {

    WarrantyMaintainEntity save(WarrantyMaintainEntity warrantyMaintainEntity);

    boolean delete(WarrantyMaintainInfo warrantyMaintainInfo);

    boolean delete(String objId);

    WarrantyMaintainInfo findOne(String objId);

    List<WarrantyMaintainInfo> findAllByWarrantyMaintenanceObjId(String objId);

    List<WarrantyMaintainEntity> saveList(List<WarrantyMaintainEntity> list);
}
