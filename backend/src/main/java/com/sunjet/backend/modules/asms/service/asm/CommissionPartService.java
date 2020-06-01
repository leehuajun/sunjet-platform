package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.CommissionPartEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ActivityMaintenanceCommissionPartView;
import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceCommissionPartView;
import com.sunjet.dto.asms.asm.CommissionPartInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface CommissionPartService {

    CommissionPartInfo save(CommissionPartInfo commissionPartInfo);

    boolean delete(CommissionPartInfo commissionPartInfo);

    boolean delete(String objId);

    CommissionPartInfo findOne(String objId);

    List<CommissionPartInfo> findAllByWarrantyMaintenanceObjId(String objId);

    boolean deleteByActivityMaintenanceId(String objId);

    List<CommissionPartInfo> findAllByActivityMaintenanceObjId(String objId);

    List<WarrantyMaintenanceCommissionPartView> findAllByWarrantyMaintenanceIdList(List<String> warrantyMaintenanceIdList);

    List<ActivityMaintenanceCommissionPartView> findAllByActivityMaintenanceIdList(List<String> activityMaintenanceIdList);

    Boolean deleteByWarrantyMaintenanceObjId(String objId);

    List<CommissionPartEntity> saveList(List<CommissionPartEntity> commissionPartEntityList);
}
