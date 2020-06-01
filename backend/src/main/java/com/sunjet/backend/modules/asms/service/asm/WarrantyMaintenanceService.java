package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 三包服务单
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintenanceService extends BaseService {

    WarrantyMaintenanceInfo save(WarrantyMaintenanceInfo warrantyMaintenanceInfo);

    boolean delete(WarrantyMaintenanceInfo warrantyMaintenanceInfo);

    boolean delete(String objId);

    WarrantyMaintenanceInfo findOne(String objId);

    PageResult<WarrantyMaintenanceView> getPageList(PageParam<WarrantyMaintenanceItem> pageParam);

    WarrantyMaintenanceInfo findOneWithOthersBySrcDocNo(String srcDocNo);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllObjIdsByVehicleId(List<String> vehicleObjIds);

    List<WarrantyMaintenanceInfo> findAllByVehicleId(String vehicleId);

    Boolean desertTask(String objId);
}
