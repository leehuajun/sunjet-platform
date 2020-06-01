package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.view.FirstMaintenanceView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 首保
 * Created by lhj on 16/9/17.
 */
public interface FirstMaintenanceService extends BaseService {

    FirstMaintenanceInfo save(FirstMaintenanceInfo firstMaintenanceInfo);

    boolean delete(FirstMaintenanceInfo firstMaintenanceInfo);

    boolean delete(String objId);

    FirstMaintenanceInfo findOne(String objId);


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<FirstMaintenanceView> getPageList(PageParam<FirstMaintenanceItem> pageParam);


    /**
     * 获取首保服务单信息
     *
     * @param businessId
     * @return
     */
    public FirstMaintenanceInfo findOneWithGoOutsById(String businessId);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllIdByVehcicleIds(List<String> vehicleObjIds);

    FirstMaintenanceInfo findOneByVehicleId(String vehicleId);

    Boolean desertTask(String objId);
}
