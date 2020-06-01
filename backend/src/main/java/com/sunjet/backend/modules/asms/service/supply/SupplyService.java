package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.backend.modules.asms.entity.supply.view.SupplyView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 供货单/发货单
 * Created by lhj on 16/9/17.
 */
public interface SupplyService extends BaseService {

    SupplyInfo save(SupplyInfo supplyInfo);

    boolean delete(SupplyInfo supplyInfo);

    boolean delete(String objId);

    SupplyInfo findOne(String objId);

    //void save(SupplyEntity supplyRequest);
    //
    //SupplyEntity getSupplyByID(String id);
    //
    //SupplyEntity findSupplyWithPartsById(String objId);
    //
    //boolean deleteEntity(SupplyEntity supplyRequest);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyView> getPageList(PageParam<SupplyItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);


    List<String> findAllIdByVehicleIds(List<String> vehicleObjIds);

    Boolean desertTask(String objId);
}
